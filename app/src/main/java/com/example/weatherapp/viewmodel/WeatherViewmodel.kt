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

    private val weatherService = WeatherService.getInstance()

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = weatherService.getWeather(city)
                _weatherData.postValue(response)
            } catch (e: Exception) {
                Log.e("WeatherViewmodel", "Error fetching data", e)
            }
        }
    }
}