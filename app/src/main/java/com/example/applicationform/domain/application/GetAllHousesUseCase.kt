package com.example.applicationform.domain.application

import com.example.applicationform.domain.models.House
import com.example.applicationform.domain.models.Street
import com.example.applicationform.domain.repository.ApplicationRepository

class GetAllHousesUseCase(private val applicationRepository: ApplicationRepository) {

    suspend fun execute(streetId: String): List<House> =
        applicationRepository.getAllHouses(streetId)
}