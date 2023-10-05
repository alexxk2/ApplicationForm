package com.example.applicationform.data.network.dto

import com.google.gson.annotations.SerializedName

data class HouseDto(
    @SerializedName("house") val houseName: String,
    @SerializedName("house_id") val houseId: String
)
