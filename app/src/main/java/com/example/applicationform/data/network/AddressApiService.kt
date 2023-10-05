package com.example.applicationform.data.network

import com.example.applicationform.data.network.dto.HouseDto
import com.example.applicationform.data.network.dto.StreetDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressApiService {


    @GET("/v2/utils/get/allStreets/")
    suspend fun getAllStreets(): Response<List<StreetDto>>

    @GET("/v2/utils/get/houses")
    suspend fun getAllHouses(@Query("street_id") streetId: String): Response<List<HouseDto>>
}