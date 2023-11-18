package com.camihruiz24.flight_search.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

@Entity(tableName = "favorite")
data class FavoriteFlight(
    @ColumnInfo(name = "departure_code")
    val departureCode: String,
    @ColumnInfo(name = "destination_code")
    val destinationCode: String,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = (departureCode + destinationCode).hashCode(),
) {
    init {
        require(departureCode != destinationCode)
        require(departureCode.length == 3)
        require(destinationCode.length == 3)
    }
}

suspend fun List<FavoriteFlight>.toFlightList(airportsRepository: AirportsRepository): List<Flight> = coroutineScope {
    map { favFlight ->
        val (departureName: String, departurePassengers: Long) =
            airportsRepository.getAirportNameAndPassengersByIataCode(favFlight.departureCode).first()
        val (destinationName: String, destinationPassengers: Long) =
            airportsRepository.getAirportNameAndPassengersByIataCode(favFlight.destinationCode).first()
        Flight(
            departureName = departureName,
            departureCode = favFlight.departureCode,
            departurePassengers = departurePassengers,
            destinationName = destinationName,
            destinationCode = favFlight.destinationCode,
            destinationPassengers = destinationPassengers,
            isFavorite = true
        )
    }
}

