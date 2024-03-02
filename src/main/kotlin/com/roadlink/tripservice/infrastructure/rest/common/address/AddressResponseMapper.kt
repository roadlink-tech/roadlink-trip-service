package com.roadlink.tripservice.infrastructure.rest.common.address

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.common.address.Address


object AddressResponseMapper {
    fun map(address: Address) =
        AddressResponse(
            location = LocationResponseMapper.map(address.location),
            fullAddress = address.fullAddress,
            street = address.street,
            city = address.city,
            country = address.country,
            housenumber = address.houseNumber,
        )
}

object LocationResponseMapper {
    fun map(location: Location) =
        LocationResponse(
            latitude = location.latitude,
            longitude = location.longitude,
        )
}