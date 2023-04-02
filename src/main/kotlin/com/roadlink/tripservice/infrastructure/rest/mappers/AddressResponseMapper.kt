package com.roadlink.tripservice.infrastructure.rest.mappers

import com.roadlink.tripservice.domain.trip.Address
import com.roadlink.tripservice.infrastructure.rest.responses.AddressResponse

object AddressResponseMapper {
    fun map(address: Address) =
        AddressResponse(
            location = LocationResponseMapper.map(address.location),
            fullAddress = address.fullAddress,
            street = address.street,
            city = address.city,
            country = address.country,
            housenumber = address.housenumber,
        )
}