package com.example.huertoavance8.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.example.huertoavance8.data.remote.WeatherApiClient

// Resultado sellado para manejar éxito / error
sealed class WeatherResult {
    data class Success(val temperatureC: Double, val descripcion: String) : WeatherResult()
    data class Error(val mensaje: String) : WeatherResult()
}

class WeatherRepository(private val context: Context) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission") // ya pedimos permiso en la UI
    suspend fun getWeatherForCurrentLocation(): WeatherResult {

        // 1) Elegir proveedor (GPS o red)
        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER
            else -> return WeatherResult.Error("La ubicación está desactivada en el dispositivo")
        }

        // 2) Obtener última ubicación conocida
        val lastLocation: Location? = locationManager.getLastKnownLocation(provider)
        val location = lastLocation
            ?: return WeatherResult.Error("No se pudo obtener la ubicación actual")

        val lat = location.latitude
        val lon = location.longitude

        return try {
            // 3) Llamar a Open-Meteo
            val api = WeatherApiClient.api
            val response = api.getCurrentWeather(lat, lon)

            val current = response.current
                ?: return WeatherResult.Error("La API no devolvió datos actuales")

            val temp = current.temperature_2m ?: 0.0
            val code = current.weather_code ?: 0

            val descripcion = mapWeatherCodeToDescription(code)

            WeatherResult.Success(temp, descripcion)
        } catch (e: Exception) {
            WeatherResult.Error("Error al obtener el clima: ${e.message}")
        }
    }

    // Mapea el weather_code de Open-Meteo a texto en español
    private fun mapWeatherCodeToDescription(code: Int): String {
        return when (code) {
            0 -> "Cielo despejado"
            1, 2 -> "Mayormente despejado"
            3 -> "Nublado"
            45, 48 -> "Niebla"
            51, 53, 55 -> "Llovizna"
            61, 63, 65 -> "Lluvia"
            71, 73, 75, 77 -> "Nieve"
            80, 81, 82 -> "Chubascos de lluvia"
            85, 86 -> "Chubascos de nieve"
            95 -> "Tormenta eléctrica"
            96, 99 -> "Tormenta con granizo"
            else -> "Condición desconocida"
        }
    }
}
