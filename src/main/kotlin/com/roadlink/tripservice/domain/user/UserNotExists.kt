package com.roadlink.tripservice.domain.user

class UserNotExists(val userId: String) : RuntimeException("User '$userId' not exists")
