package com.roadlink.tripservice.domain

class UserNotExists(val userId: String) : RuntimeException("User '$userId' not exists")
