package com.attia.sunny.viewModel

import androidx.lifecycle.ViewModel
import com.attia.sunny.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(private val cityRepository: CityRepository): ViewModel() {

    fun loadCity(q: String, limit: Int) =
        cityRepository.getCities(q, limit)
}