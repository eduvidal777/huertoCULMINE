package com.example.huertoavance8.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class WeatherUiState(
    val temperatura: Double? = null,
    val descripcion: String? = null,
    val ultimaActualizacion: String? = null,
    val pronosticoHoras: List<Double> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var weatherState by remember {
        mutableStateOf(
            WeatherUiState(
                cargando = true
            )
        )
    }

    // Cargar clima al entrar
    LaunchedEffect(Unit) {
        scope.launch {
            weatherState = weatherState.copy(cargando = true, error = null)
            weatherState = cargarClima()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Huerto Hogar") },
                actions = {
                    TextButton(onClick = { navController.navigate("perfil") }) {
                        Text("Perfil", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🌿 Bienvenido, usuario 🌿",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CARD DE CLIMA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        "Clima actual en Santiago ☀️",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    when {
                        weatherState.cargando -> {
                            CircularProgressIndicator()
                        }

                        weatherState.error != null -> {
                            Text(
                                text = "Error al obtener el clima:\n${weatherState.error}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {
                            Text(
                                text = "Temperatura: ${weatherState.temperatura} °C",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Estado del cielo: ${weatherState.descripcion}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Actualizado a las ${weatherState.ultimaActualizacion}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Pronóstico rápido:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            if (weatherState.pronosticoHoras.size >= 3) {
                                Text("En 1 hora: ${weatherState.pronosticoHoras[0]} °C")
                                Text("En 3 horas: ${weatherState.pronosticoHoras[1]} °C")
                                Text("En 6 horas: ${weatherState.pronosticoHoras[2]} °C")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                weatherState = weatherState.copy(cargando = true, error = null)
                                weatherState = cargarClima()
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Actualizar ahora")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BOTONES DE NAVEGACIÓN
            Button(
                onClick = { navController.navigate("productos") },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Ver Productos")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("carrito") },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Ir al Carrito")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("perfil") },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Mi Perfil")
            }
        }
    }
}

/**
 * Llama a la API de Open-Meteo para la lat/lon de Santiago
 * y devuelve el estado de la UI con hora LOCAL Chile.
 */
private suspend fun cargarClima(): WeatherUiState = withContext(Dispatchers.IO) {
    try {
        // 👇 IMPORTANTE: timezone=America/Santiago para que venga en hora chilena
        val url =
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude=-33.4569&longitude=-70.6483" +
                    "&current_weather=true" +
                    "&hourly=temperature_2m" +
                    "&timezone=America%2FSantiago"

        val response = URL(url).readText()
        val json = JSONObject(response)

        val current = json.getJSONObject("current_weather")
        val temp = current.getDouble("temperature")
        val weatherCode = current.getInt("weathercode")
        val timeStr = current.getString("time") // ej: 2025-11-30T02:23

        // 👉 Formateamos solo HH:MM
        val horaLocal = timeStr.substringAfter('T').substring(0, 5)

        val descripcion = when (weatherCode) {
            0 -> "Despejado"
            1, 2 -> "Parcialmente nublado"
            3 -> "Nublado"
            in 51..67 -> "Llovizna"
            in 71..77 -> "Nieve"
            in 80..82 -> "Chubascos"
            in 95..99 -> "Tormenta"
            else -> "Condición desconocida"
        }

        // Pronóstico rápido: buscamos la posición de la hora actual
        val hourly = json.getJSONObject("hourly")
        val horas = hourly.getJSONArray("time")
        val temps = hourly.getJSONArray("temperature_2m")

        // Índice de la hora actual dentro del arreglo
        var idxActual = 0
        for (i in 0 until horas.length()) {
            if (horas.getString(i) == timeStr) {
                idxActual = i
                break
            }
        }

        fun tempEn(offset: Int): Double {
            val idx = (idxActual + offset).coerceAtMost(temps.length() - 1)
            return temps.getDouble(idx)
        }

        val pronostico = listOf(
            tempEn(1),  // +1 hora
            tempEn(3),  // +3 horas
            tempEn(6)   // +6 horas
        )

        WeatherUiState(
            temperatura = temp,
            descripcion = descripcion,
            ultimaActualizacion = horaLocal,
            pronosticoHoras = pronostico,
            cargando = false,
            error = null
        )
    } catch (e: Exception) {
        WeatherUiState(
            cargando = false,
            error = e.localizedMessage ?: "Error desconocido"
        )
    }
}
