package com.example.applicationform.domain.application

import com.example.applicationform.domain.models.Street
import com.example.applicationform.domain.repository.ApplicationRepository

class GetAllStreetsUseCase(private val applicationRepository: ApplicationRepository) {
    suspend fun execute(): List<Street> = applicationRepository.getAllStreets()
}