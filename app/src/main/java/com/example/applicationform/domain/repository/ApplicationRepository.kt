package com.example.applicationform.domain.repository

import com.example.applicationform.domain.models.House
import com.example.applicationform.domain.models.Street

interface ApplicationRepository {

    suspend fun getAllStreets(): List<Street>
    suspend fun getAllHouses(streetId: String): List<House>
}