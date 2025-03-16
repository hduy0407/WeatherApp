package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.model.WeatherService
import kotlinx.coroutines.launch

class WeatherViewmodel: ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponse?>()
    val weatherData: LiveData<WeatherResponse?> get() = _weatherData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    private val weatherService = WeatherService.getInstance()

    fun fetchWeather(city: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response = weatherService.getWeather(city)
                _weatherData.postValue(response)
                _errorMessage.value = null
            } catch (e: Exception) {
                Log.e("WeatherViewmodel", "Error fetching data", e)
                _errorMessage.value = "Failed to load weather data. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
