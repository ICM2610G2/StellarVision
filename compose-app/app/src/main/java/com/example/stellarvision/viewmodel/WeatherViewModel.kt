package com.example.stellarvision.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperatureC: Double? = null,
    val condition: String = "",
    val weatherCode: Int? = null,
    val cloudCover: Int? = null,
    val humidity: Int? = null,
    val error: String? = null
)

class WeatherViewModel : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState())
    val state = _state.asStateFlow()

    private var lastFetchTime: Long = 0L

    fun loadCurrentWeather(
        latitude: Double,
        longitude: Double,
        force: Boolean = false
    ) {
        if (latitude == 0.0 && longitude == 0.0) return

        val now = System.currentTimeMillis()

        if (!force && now - lastFetchTime < 10 * 60 * 1000 && _state.value.temperatureC != null) {
            return
        }

        lastFetchTime = now

        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val url = "https://api.open-meteo.com/v1/forecast" +
                        "?latitude=$latitude" +
                        "&longitude=$longitude" +
                        "&current=temperature_2m,relative_humidity_2m,weather_code,cloud_cover" +
                        "&timezone=auto"

                val response = URL(url).readText()
                val json = JSONObject(response)
                val current = json.getJSONObject("current")

                val temperature = current.optDouble("temperature_2m")
                val weatherCode = current.optInt("weather_code")
                val cloudCover = current.optInt("cloud_cover")
                val humidity = current.optInt("relative_humidity_2m")

                _state.update {
                    WeatherUiState(
                        isLoading = false,
                        temperatureC = temperature,
                        condition = weatherCodeToText(weatherCode),
                        weatherCode = weatherCode,
                        cloudCover = cloudCover,
                        humidity = humidity,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cargar el clima"
                    )
                }
            }
        }
    }

    private fun weatherCodeToText(code: Int): String {
        return when (code) {
            0 -> "Cielo despejado"
            1 -> "Mayormente despejado"
            2 -> "Parcialmente nublado"
            3 -> "Nublado"
            45, 48 -> "Niebla"
            51, 53, 55 -> "Llovizna"
            56, 57 -> "Llovizna helada"
            61, 63, 65 -> "Lluvia"
            66, 67 -> "Lluvia helada"
            71, 73, 75 -> "Nieve"
            77 -> "Granizo de nieve"
            80, 81, 82 -> "Chubascos"
            85, 86 -> "Chubascos de nieve"
            95 -> "Tormenta"
            96, 99 -> "Tormenta con granizo"
            else -> "Clima desconocido"
        }
    }
}