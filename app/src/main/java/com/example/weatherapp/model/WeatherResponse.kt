package com.example.weatherapp.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

// I put it here just for study purpose
const val API_KEY = "87097291fc5944447789d2625cc4fa76"

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherDetail>
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class WeatherDetail(
    val description: String
)

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") unit: String = "metric"
    ): WeatherResponse

    companion object {
        private var weatherService: WeatherService? = null

        fun getInstance(): WeatherService {
            if (weatherService == null) {
                weatherService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WeatherService::class.java)
            }
            return weatherService!!
        }
    }
}