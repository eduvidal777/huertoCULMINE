package com.example.huertoavance8.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

// Respuesta de Open-Meteo (simplificada)
// Ejemplo de URL:
// https://api.open-meteo.com/v1/forecast?latitude=-33.45&longitude=-70.67&current=temperature_2m,weather_code&timezone=auto
data class CurrentWeather(
    val time: String?,
    val temperature_2m: Double?,
    val weather_code: Int?
)

data class WeatherApiResponse(
    val latitude: Double?,
    val longitude: Double?,
    val current: CurrentWeather?
)

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        // Pedimos solo lo que necesitamos
        @Query("current") current: String = "temperature_2m,weather_code",
        @Query("timezone") timezone: String = "auto"
    ): WeatherApiResponse
}
