package com.fdt.tripservice.domain.entity

class Sections(private val path: List<Location>) {

    private var sections = mutableListOf<Section>()

    init {
        for (i in 0 until path.lastIndex) {
            sections.add(Section(path[i], path[i + 1], 0))
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

//    operator fun contains(location: Location): Boolean =
//        sections.find { it.initial == location || it.final == location } != null

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
