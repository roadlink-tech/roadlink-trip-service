package com.roadlink.tripservice.dev_tools.domain

class GeoapifyPointNotExists(name: String) : RuntimeException("Geoapify point not exists. Name: $name")