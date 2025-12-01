package com.example.huertoavance8.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertoavance8.data.repository.WeatherRepository
import com.example.huertoavance8.data.repository.WeatherResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperaturaC: Double? = null,
    val descripcion: String = "",
    val error: String? = null
)

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> get() = _uiState

    fun cargarClima() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = repository.getWeatherForCurrentLocation()) {
                is WeatherResult.Success -> {
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        temperaturaC = result.temperatureC,
                        descripcion = result.descripcion,
                        error = null
                    )
                }

                is WeatherResult.Error -> {
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        temperaturaC = null,
                        descripcion = "",
                        error = result.mensaje
                    )
                }
            }
        }
    }

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = message
        )
    }
}
