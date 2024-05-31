package com.attia.sunny.repository

import com.attia.sunny.network.ApiServices
import javax.inject.Inject

class SunnyRepository @Inject constructor(private val apiService: ApiServices) {
    fun getCurrentWeather(lat: Double, lon: Double, units: String) =
        apiService.getCurrentWeather(lat, lon, units, "5da65eb0a8379d20f675279ee30bf33d")

    fun getForecastWeather(lat: Double, lon: Double, units: String) =
        apiService.getForecastWeather(lat, lon, units, "5da65eb0a8379d20f675279ee30bf33d")
}