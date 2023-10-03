package com.example.applicationform.data.network

import com.example.applicationform.data.network.dto.HouseDto
import com.example.applicationform.data.network.dto.StreetDto

interface NetworkClient {

    suspend fun getAllStreets(): List<StreetDto>
    suspend fun getAllHouses(streetId: String): List<HouseDto>
}