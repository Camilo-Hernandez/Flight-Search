package com.camihruiz24.flight_search.data.model

import com.camihruiz24.flight_search.data.database.FavoriteFlight

/**
 * The [Flight] data class is one of the main models in the UI.
 */
data class Flight(
    val departureCode: String,
    val departureName: String,
    val numberOfPassengers: Long,
    val destinationCode: String,
    val destinationName: String,
) {
    init {
        require(departureCode != destinationCode)
    }

    fun toDBModel(): FavoriteFlight = FavoriteFlight(
        0,
        departureCode,
        destinationCode
    )
}
