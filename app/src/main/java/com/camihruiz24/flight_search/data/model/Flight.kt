package com.camihruiz24.flight_search.data.model

import com.camihruiz24.flight_search.data.database.FavoriteFlight

/**
 * The [Flight] data class is one of the main models in the UI.
 */
data class Flight(
    val departureName: String,
    val departureCode: String,
    val departurePassengers: Long,
    val destinationName: String,
    val destinationCode: String,
    val destinationPassengers: Long,
    val isFavorite: Boolean = false,
) {

    init {
        require(departureCode != destinationCode)
        require(departureCode.length == 3)
        require(destinationCode.length == 3)
    }

    fun toDBModel(): FavoriteFlight = FavoriteFlight(
        departureCode,
        destinationCode
    )

}
