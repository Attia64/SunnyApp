package com.attia.sunny.repository

import com.attia.sunny.network.ApiServices
import javax.inject.Inject

class CityRepository @Inject constructor(private val apiService: ApiServices) {

    fun getCities(q: String, limit: Int) =
        apiService.getCitiesList(q, limit, "5da65eb0a8379d20f675279ee30bf33d")
}