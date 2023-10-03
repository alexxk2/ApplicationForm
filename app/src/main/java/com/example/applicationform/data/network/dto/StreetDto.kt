package com.example.applicationform.data.network.dto

import com.google.gson.annotations.SerializedName

data class StreetDto(
    @SerializedName("street") val streetName: String,
    @SerializedName("street_id") val streetId: String
)
