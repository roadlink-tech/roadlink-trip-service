package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.infrastructure.persistence.common.Jts
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import java.time.Instant

class BruteForceSearchEngine(
    private val sectionRepository: SectionRepository,
    private val circleSearchAreaCreator: SearchAreaCreator<JtsCircle>,
) : SearchEngine {

    private val geometryFactory = GeometryFactory(PrecisionModel(), Jts.SRID)

    override fun search(
        departure: Location,
        arrival: Location,
        at: Instant
    ): List<TripSearchPlanResult> {
        val circleSearchAreaRadius = SearchRadiusGenerator(departure, arrival)
        val tripSearchPlanResults = mutableListOf<TripSearchPlanResult>()
        val stack = mutableListOf<TripSearchPlanResult>()
        val initialDepartureSearchArea =
            circleSearchAreaCreator.from(departure, circleSearchAreaRadius)
        sectionRepository.findNextSectionsIn(initialDepartureSearchArea.value, at)
            .forEach { nextSection ->
                val tripSearchPlanResult = TripSearchPlanResult(listOf(nextSection))
                stack.add(tripSearchPlanResult)
            }

        while (stack.isNotEmpty()) {
            val actualTripPlan = stack.removeLast()
            val actualSection = actualTripPlan.last()

            val arrivalSearchArea =
                circleSearchAreaCreator.from(arrival, circleSearchAreaRadius).value
            if (arrivalSearchArea.contains(toPoint(actualSection.arrival()))) {
                tripSearchPlanResults.add(actualTripPlan)
            } else {
                val departureSearchArea =
                    circleSearchAreaCreator.from(actualSection.arrival(), circleSearchAreaRadius)
                val estimatedArrivalTimeAtArrival = actualSection.estimatedArrivalTimeAtArrival()
                sectionRepository.findNextSectionsIn(
                    departureSearchArea.value,
                    estimatedArrivalTimeAtArrival
                ).forEach { nextSection ->
                    stack.add(actualTripPlan + nextSection)
                }
            }
        }

        return tripSearchPlanResults.sortedBy { it.distance() }
    }

    private fun toPoint(location: Location): Point {
        return geometryFactory.createPoint(Coordinate(location.longitude, location.latitude))
    }
}
