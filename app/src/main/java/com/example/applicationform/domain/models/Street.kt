package com.example.applicationform.domain.models

data class Street(
    val streetId: String,
    val streetName: String
){
    override fun toString(): String {
        return streetName
    }
}
