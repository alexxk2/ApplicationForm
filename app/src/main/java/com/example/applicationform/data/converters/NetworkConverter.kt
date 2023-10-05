package com.example.applicationform.data.converters

import com.example.applicationform.data.network.dto.HouseDto
import com.example.applicationform.data.network.dto.StreetDto
import com.example.applicationform.domain.models.House
import com.example.applicationform.domain.models.Street

class NetworkConverter {

    fun convertHouseToDomain(houseDto: HouseDto): House {
        with(houseDto) {
            return House(
                houseId = houseId,
                houseName = houseName
            )
        }
    }

    fun convertStreetToDomain(streetDto: StreetDto): Street {
        with(streetDto) {
            return Street(
                streetId = streetId,
                streetName = streetName
            )
        }
    }
}