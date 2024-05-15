package com.roadlink.tripservice.usecases.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.domain.trip_search.algorithm.SearchEngine
import com.roadlink.tripservice.domain.trip_search.filter.Filter
import com.roadlink.tripservice.domain.trip_search.filter.FilterService
import com.roadlink.tripservice.domain.user.UserError
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.usecases.UseCase
import java.time.Instant
import java.util.*

class SearchTrip(
    private val userRepository: UserRepository,
    private val searchEngine: SearchEngine,
    private val filterService: FilterService,
) : UseCase<SearchTrip.Input, SearchTrip.Output> {

    override operator fun invoke(input: Input): Output {
        return userRepository.findByUserId(input.callerId.toString())?.let { user ->
            searchEngine.search(input.departure, input.arrival, input.at)
                .let { results -> filterService.evaluate(user, results, input.domainFilters()) }
                .let { filteredResults -> Output(filteredResults) }
        } ?: throw UserError.NotExists(input.callerId.toString())
    }
    
    data class Output(val result: List<TripSearchPlanResult>)
    data class Input(
        val callerId: UUID,
        val departure: Location,
        val arrival: Location,
        val at: Instant,
        val filters: List<String> = emptyList()
    ) {
        fun domainFilters(): Set<Filter> {
            return filters.map { Filter.valueOf(it) }.toSet()
        }
    }
}
