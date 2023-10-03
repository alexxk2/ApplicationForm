package com.example.applicationform.data.network.impl

import com.example.applicationform.data.network.AddressApiService
import com.example.applicationform.data.network.NetworkClient
import com.example.applicationform.data.network.dto.HouseDto
import com.example.applicationform.data.network.dto.StreetDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl: NetworkClient {


    private val baseUrl = "https://stat-api.airnet.ru"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: AddressApiService by lazy {
        retrofit.create(AddressApiService::class.java)
    }


    override suspend fun getAllStreets(): List<StreetDto> {
        val response = retrofitService.getAllStreets()
        return if (response.code() == 200){
            response.body()?.toList()?: emptyList()
        }
        else emptyList()
    }

    override suspend fun getAllHouses(streetId: String): List<HouseDto> {
        val response = retrofitService.getAllHouses(streetId)
        return if (response.code() == 200){
            response.body()?.toList()?: emptyList()
        }
        else emptyList()
    }
}