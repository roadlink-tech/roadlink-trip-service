package com.roadlink.tripservice.domain.trip_search.filter

enum class Filter {
    /**
     * Search Preferences: Filters that are applied to the search results to customize the displayed
     * options according to user preferences.
     */
    UPCOMING_YEAR,

    /**
     * Trip Rules: Rules established by the driver of a trip, forming part of the trip itself, which
     * can be used as filters to customize the experience.
     */
    PET_ALLOWED, NO_SMOKING,

    /**
     * Trip Visibility Restriction: Restrictions established by drivers that apply to the results of
     * any search, controlling who can view and participate in the trip.
     */
    ONLY_FRIENDS, ONLY_WOMEN
}