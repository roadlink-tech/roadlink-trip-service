package com.roadlink.tripservice.infrastructure

import com.roadlink.tripservice.domain.IdGenerator
import java.util.*

class UUIDIdGenerator : IdGenerator {
    override fun id(): String {
        return UUID.randomUUID().toString()
    }
}