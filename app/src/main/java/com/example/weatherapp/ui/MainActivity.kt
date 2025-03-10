package com.example.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewmodel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(modifier: Modifier = Modifier, viewModel: WeatherViewmodel = viewModel()) {
    var city by remember { mutableStateOf("") }
    val weatherData by viewModel.weatherData.observeAsState()

    Column (
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = {city = it},
            label = { Text("Enter city name")},
            modifier = Modifier.fillMaxWidth().padding(8.dp)

        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.fetchWeather(city) }, modifier = Modifier.fillMaxWidth()) {
            Text("Fetch Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        weatherData?.let {
            Text(text = "Temperature: ${it.main.temp}°C")
            Text(text = "Humidity: ${it.main.humidity}%")
            Text(text = "Condition: ${it.weather[0].description}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherScreen()
    }
}