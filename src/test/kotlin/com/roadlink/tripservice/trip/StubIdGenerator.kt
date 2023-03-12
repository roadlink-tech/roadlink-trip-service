package com.roadlink.tripservice.trip

import com.roadlink.tripservice.domain.IdGenerator

class StubIdGenerator(
    private val idsToGenerate: MutableList<String> = mutableListOf(),
) : IdGenerator {
    override fun id(): String  =
        idsToGenerate.removeLast()

    fun nextIdToGenerate(id: String) {
        idsToGenerate.add(id)
    }
}