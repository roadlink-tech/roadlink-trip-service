package com.fdt.tripservice.usecases

import com.fdt.tripservice.domain.trip.Location
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

val INITIAL_PHEROMONE = 1.0

class SearchTrip(
    private val nextLocationsFrom: NextLocationsFrom,
    private val pickWithProbability: PickWithProbability,
) {

    operator fun invoke(request: Request) {
        val antSystem = AntSystem(
            nextLocationsFrom,
            pickWithProbability,
            ::probabilitiesToNextLocations,
            ::distance,
        )

        antSystem(
            departure = request.departure,
            arrival = request.arrival,
        )
    }

    data class Request(
        val departure: Location,
        val arrival: Location,
    )
}

interface NextLocationsFrom {
    operator fun invoke(location: Location): Set<Location>
}

class FakeNextLocationsFrom(
    private val nexts: MutableMap<Location, Set<Location>>,
) : NextLocationsFrom {
    override fun invoke(location: Location): Set<Location> {
        return nexts[location] ?: emptySet()
    }
}

class AntSystem(
    private val nextLocationsFrom: NextLocationsFrom,
    private val pickWithProbability: PickWithProbability,
    private val probabilitiesToNextLocations: (nexts: Set<Triple<Location, Double, Double>>) -> Set<Pair<Location, Double>>,
    private val distance: (from: Location, to: Location) -> Double,
) {

    private val pheromone: MutableMap<Location, MutableMap<Location, Double>> = mutableMapOf()

    operator fun invoke(departure: Location, arrival: Location) {
        repeat(1000) {
            val (trip, distance) = runAnt(departure, arrival)
            updatePheromone(trip, distance)
        }

        chooseTourHighestPheromone(departure, arrival)
    }

    private fun runAnt(departure: Location, arrival: Location): Pair<List<Location>, Double> {
        var actual = departure
        val trip = mutableListOf<Location>(actual)
        var distance = 0.0

        while (actual != arrival) {
            val next = nextLocation(actual)
            trip.add(next)
            distance += distance(actual, next)

            actual = next
        }

        return trip to distance
    }

    private fun nextLocation(actual: Location): Location {
        val nextLocations = nextLocationsFrom(actual)
        val probabilities = probabilitiesToNextLocations(nextLocations.map { Triple(it, distance(actual, it), pheromone(actual, it)) }.toSet())
        val choices = probabilities.map { PickWithProbability.Choice(it.first, it.second) }.toSet()
        return pickWithProbability(choices)
    }

    private fun pheromone(from: Location, to: Location): Double {
        return pheromone.get(from)?.get(to) ?: INITIAL_PHEROMONE
    }

    private fun updatePheromone(trip: List<Location>, distance: Double) {
        for (i in 0 until trip.lastIndex) {
            pheromone
                .getOrPut(trip[i]) { mutableMapOf(trip[i+1] to INITIAL_PHEROMONE) }
                .compute(trip[i+1]) { _, pheromone -> pheromone?.plus(1 / distance) ?: (1 / distance) }
        }
    }

    private fun chooseTourHighestPheromone(departure: Location, arrival: Location) {
        var actual = departure
        val trip = mutableListOf<Location>(actual)

        while (actual != arrival) {
            val next = pheromone[actual]!!.maxBy { it.value }.key
            trip.add(next)

            actual = next
        }
        trip.forEach { println(it) }
    }
}

fun probabilitiesToNextLocations(nexts: Set<Triple<Location, Double, Double>>): Set<Pair<Location, Double>> {
    val denominator = nexts.sumOf { next -> (1/next.second) * next.third }

    return nexts
        .map { next ->
            val probability = ((1/next.second) * next.third) / denominator
            next.first to probability
        }.toSet()
}

fun distance(from: Location, to: Location): Double {
    return sqrt((to.latitude-from.latitude).pow(2) + (to.longitude-from.longitude).pow(2))
}

interface UniformDoubleGenerator {
    fun next(): Double
}

class RandomUniformDoubleGenerator : UniformDoubleGenerator {
    override fun next(): Double = Random.nextDouble()
}

class PickWithProbability(private val uniformDoubleGenerator: UniformDoubleGenerator) {
    operator fun invoke(choices: Set<Choice>): Location {
        val u = uniformDoubleGenerator.next()
        var accumProb = 0.0

        choices.forEach { choice ->
            if (u in accumProb..accumProb+choice.probability)
                return choice.location

            accumProb+=choice.probability
        }

        return choices.random().location
    }

    data class Choice(val location: Location, val probability: Double)
}
