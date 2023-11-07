package com.camihruiz24.flight_search.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camihruiz24.flight_search.data.model.Flight

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "iata_code")
    val iataCode: String,
    @ColumnInfo(name = "passengers")
    val passengers: Long,
) {
    init {
        require(iataCode.length == 3)
    }
}

/**
 * Creates a list of flights for every airport given in [this] (the list of airports).
 * It is assumed that every airport has flights to every other airport in the DB except to itself.
 * [totalAirports] should be given from the [AirportsRepository]
 */
internal fun List<Airport>.toCompleteFlightsList(
    totalAirports: List<Airport>,
    favoriteFlights: List<FavoriteFlight>
): List<Flight> =
    mutableListOf<Flight>().let { flightList ->
        this.forEach { departureAirport ->
            totalAirports.forEach { destinationAirport ->
                if (departureAirport.iataCode != destinationAirport.iataCode)
                    try {
                        Flight(
                            departureName = departureAirport.name,
                            departureCode = departureAirport.iataCode,
                            departurePassengers = departureAirport.passengers,
                            destinationName = destinationAirport.name,
                            destinationCode = destinationAirport.iataCode,
                            destinationPassengers = destinationAirport.passengers,
                            isFavorite = run {
                                favoriteFlights.any {
                                    it.id == (departureAirport.iataCode + destinationAirport.iataCode).hashCode()
                                }
                            }
                        ).also { flight ->
                            flightList.add(flight)
                        }
                    } catch (_: IllegalArgumentException) {
                        // Do not create the object
                    }
            }
        }
        flightList.toList()
    }

