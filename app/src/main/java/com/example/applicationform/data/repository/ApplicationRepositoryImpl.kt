package com.example.applicationform.data.repository

import com.example.applicationform.data.converters.NetworkConverter
import com.example.applicationform.data.network.NetworkClient
import com.example.applicationform.domain.models.House
import com.example.applicationform.domain.models.Street
import com.example.applicationform.domain.repository.ApplicationRepository

class ApplicationRepositoryImpl(
    private val converter: NetworkConverter,
    private val networkClient: NetworkClient
) : ApplicationRepository {

    override suspend fun getAllStreets(): List<Street> {
        val resultFromData = networkClient.getAllStreets()
        return resultFromData.map { streetDto ->
            converter.convertStreetToDomain(streetDto)
        }
    }

    override suspend fun getAllHouses(streetId: String): List<House> {
        val resultFromData = networkClient.getAllHouses(streetId)
        return resultFromData.map { houseDto ->
            converter.convertHouseToDomain(houseDto)
        }
    }
}