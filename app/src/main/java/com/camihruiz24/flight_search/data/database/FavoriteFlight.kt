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
    }
}

suspend fun List<FavoriteFlight>.toFlightList(airportsRepository: AirportsRepository): List<Flight> = coroutineScope {
    map { favFlight ->
        val (departureName: String, departurePassengers: Long) =
            airportsRepository.getAirportNameAndPassengersByIataCode(favFlight.departureCode).first()
        val (destinationName: String, _: Long) =
            airportsRepository.getAirportNameAndPassengersByIataCode(favFlight.destinationCode).first()
        Flight(
            departureCode = favFlight.departureCode,
            departureName = departureName,
            numberOfPassengers = departurePassengers,
            destinationCode = favFlight.destinationCode,
            destinationName = destinationName,
        )
    }
}

