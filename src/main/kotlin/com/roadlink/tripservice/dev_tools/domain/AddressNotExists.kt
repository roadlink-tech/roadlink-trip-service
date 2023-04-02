package com.roadlink.tripservice.dev_tools.domain

class AddressNotExists(name: String) : RuntimeException("Address not exists. Name: $name")