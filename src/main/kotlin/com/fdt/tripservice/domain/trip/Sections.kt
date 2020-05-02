package com.fdt.tripservice.domain.trip

class Sections(private val path: List<Location>) {

    private var sections = mutableListOf<Section>()

    init {
        for (i in 0 until path.lastIndex) {
            sections.add(Section(path[i], path[i + 1], mutableListOf()))
        }
    }

    operator fun get(subtrip: Subtrip): List<Section> {
        return sections.subList(
                indexOfInitial(subtrip.initial),
                indexOfFinal(subtrip.final)
        )
    }

    operator fun contains(subtrip: Subtrip) =
            subtrip.initial in path &&
                    subtrip.final in path &&
                    areInOrder(subtrip)

    operator fun contains(userId: Long) = sections.any { userId in it }

    fun unjoinPassenger(passengerId: Long) {
        sections.map { it.unjoinPassenger(passengerId) }
    }

    private fun indexOfInitial(initial: Location): Int {
        for (section in sections) {
            if (section.initial == initial) return sections.indexOf(section)
        }
        return -1
    }

    private fun indexOfFinal(final: Location): Int {
        for (section in sections) {
            if (section.final == final) return sections.indexOf(section) + 1
        }
        return -1
    }

    private fun areInOrder(subtrip: Subtrip) =
            path.indexOf(subtrip.initial) < path.indexOf(subtrip.final)

}