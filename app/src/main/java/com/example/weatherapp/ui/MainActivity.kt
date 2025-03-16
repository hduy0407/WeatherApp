package com.example.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.weatherapp.model.WeatherResponse


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "info") {
        composable("main") { WeatherScreen(navController)}
        composable("info") { InfoScreen(navController)}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavHostController, modifier: Modifier = Modifier, viewModel: WeatherViewmodel = viewModel()) {
    var city by remember { mutableStateOf("") }
    val weatherData by viewModel.weatherData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather App") }
            )
        },

        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Enter city name") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (city.isNotBlank()) {
                            viewModel.fetchWeather(city)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Get Weather", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show city name if the weather data is available
                weatherData?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)

                    ) {
                        Text(
                            text = "Weather for: $city",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        )
                        Text(
                            text = "Temperature: ${it.main.temp}°C",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue)
                        )
                        Text(
                            text = "Humidity: ${it.main.humidity}%",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                        )
                        Text(
                            text = "Condition: ${it.weather[0].description}",
                            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.secondary)
                        )
                    }
                }

                // Show loading spinner if data is being fetched
                if (isLoading) {
                    CircularProgressIndicator()
                }

                // Show error message if there's an error
                errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { navController.navigate("info") }) {
                    Text(
                        "App Info",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
fun InfoScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Weather App", style = MaterialTheme.typography.headlineLarge)
        Text("This app provides weather information fetching data from OpenWeather API")
        Button(onClick = { navController.navigate("main") }) {
            Text("Home", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherApp()
    }
}