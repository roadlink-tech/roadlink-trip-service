package com.roadlink.tripservice.domain.trip_search.filter

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip_search.TripSearchPlanResult
import com.roadlink.tripservice.domain.user.User

/**
 * The FilterService interface defines a method for filtering trip search results based on specified filters
 * and the visibility permissions of the requester.
 *
 * This service processes a list of TripSearchPlanResults and applies a list of Filters to determine
 * which results should be included in the returned list. It also considers the visibility permissions
 * of the requester. Only results that pass all the provided filters and are visible to
 * the requester are included in the output list. Results that do not meet the filter criteria or are not
 * visible to the requester are excluded.
 *
 * @param requester The User making the search request, used to check result visibility.
 * @param tripSearchPlanResult The list of TripSearchPlanResults to be evaluated.
 * @param filters The list of Filters to apply to the results.
 * @return A list of TripSearchPlanResults that meet the criteria of all provided filters.
 */
interface FilterService {
    fun evaluate(
        requester: User,
        tripSearchPlanResult: List<TripSearchPlanResult>,
        filters: List<Filter>
    ): List<TripSearchPlanResult>
}

class SearchFilterService(val tripRepository: TripRepository) : FilterService {
    override fun evaluate(
        requester: User,
        tripSearchPlanResult: List<TripSearchPlanResult>,
        filters: List<Filter>
    ): List<TripSearchPlanResult> {
        return tripSearchPlanResult.filter { result ->
            !result.listTrips(tripRepository).any { trip -> !trip.isCompliant(requester, filters) }
        }
    }
}