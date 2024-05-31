package com.attia.sunny.viewModel

import androidx.lifecycle.ViewModel
import com.attia.sunny.repository.SunnyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val sunnyRepository: SunnyRepository) :
    ViewModel() {
    fun loadCurrentWeather(lat: Double, lon: Double, units: String) =
        sunnyRepository.getCurrentWeather(lat, lon, units)

    fun loadForecastWeather(lat: Double, lon: Double, units: String) =
        sunnyRepository.getForecastWeather(lat, lon, units)
}