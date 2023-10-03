package com.example.applicationform.data.network

import com.example.applicationform.data.network.dto.HouseResponse
import com.example.applicationform.data.network.dto.StreetResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressApiService {


    @GET("/v2/utils/get/allStreets/")
    suspend fun getAllStreets(): Response<StreetResponse>

    @GET("/v2/utils/get/houses")
    suspend fun getAllHouses(@Query("street_id") streetId: String): Response<HouseResponse>
}