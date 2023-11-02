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
 * Creates a list of flights for every airport given in the DB.
 * It is assumed that every airport has flights to every other airport in the DB except to itself.
 * [totalAirports] must be given from the [AirportsRepository]
 */
internal fun List<Airport>.toCompleteFlightsList(totalAirports: List<Airport>): List<Flight> =
    mutableListOf<Flight>().let { flightList ->
        this.forEach { airportI ->
            totalAirports.forEach { airportJ ->
                if (airportI.iataCode != airportJ.iataCode)
                    try {
                        Flight(
                            departureCode = airportI.iataCode,
                            departureName = airportI.name,
                            numberOfPassengers = airportI.passengers,
                            destinationCode = airportJ.iataCode,
                            destinationName = airportJ.name
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

