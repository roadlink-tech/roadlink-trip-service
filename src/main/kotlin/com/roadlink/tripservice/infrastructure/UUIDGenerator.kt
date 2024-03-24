package com.roadlink.tripservice.infrastructure

import com.roadlink.tripservice.domain.common.IdGenerator
import java.util.*

class UUIDGenerator : IdGenerator {
    override fun id(): String {
        return UUID.randomUUID().toString()
    }
}