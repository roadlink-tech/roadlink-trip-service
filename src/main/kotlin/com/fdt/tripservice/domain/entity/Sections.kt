package com.fdt.tripservice.domain.entity

class Sections(val path: List<Location>){

    private var sections = mutableListOf<Section>()

    init{
        for (i in 0 until path.lastIndex) {
            sections.add(Section(path[i], path[i + 1], 0))
        }
    }

    private fun getIndexFromSectionAtInitial(initial: Location): Int{
        for (section in sections){
            if (section.initial == initial) return sections.indexOf(section)
        }
        return -1
    }
    private fun getIndexFromSectionAtFinal(final: Location): Int {
        for (section in sections){
            if (section.final == final) return (sections.indexOf(section) + 1)
        }
        return -1
    }

    fun getSectionsFrom(subtripSection: SubtripSection): List<Section> {
        return sections.subList(getIndexFromSectionAtInitial(subtripSection.initial),getIndexFromSectionAtFinal(subtripSection.final))
    }
}