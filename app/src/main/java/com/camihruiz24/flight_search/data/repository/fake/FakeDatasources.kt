package com.camihruiz24.flight_search.data.repository.fake

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportA
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportB
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportC
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportD
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportE
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportF

object FakeAirportDatasource {
    val airportA = Airport(
        id = 1, name = "Francisco Sá Carneiro Airport", iataCode = "OPO", passengers = 10
    )
    val airportB = Airport(
        id = 2, name = "Salamanca To po Airport", iataCode = "SAL", passengers = 20
    )
    val airportC = Airport(
        id = 3, name = "Topo Airport", iataCode = "TOP", passengers = 30
    )
    val airportD = Airport(
        id = 4, name = "Zingaku Airport", iataCode = "ZIK", passengers = 40
    )
    val airportE = Airport(
        id = 5, name = "New York Airport", iataCode = "NYA", passengers = 50
    )
    val airportF = Airport(
        id = 6, name = "Casablanca Airport", iataCode = "BLN", passengers = 60
    )
    internal val testAirports = listOf(airportA, airportB, airportC, airportD, airportE, airportF)

}

object FakeFavoriteFlightDatasource {
    // These are some of the total combinations of possible flights
    // The total number of possible flights is equal to (n-1)*s where n = total airports; s = # of searched airports
    val favFlight1 = FavoriteFlight(1, airportA.iataCode, airportB.iataCode)
    val favFlight2 = FavoriteFlight(2, airportC.iataCode, airportA.iataCode)
    val favFlight4 = FavoriteFlight(4, airportE.iataCode, airportF.iataCode)
    val favFlight5 = FavoriteFlight(5, airportA.iataCode, airportC.iataCode)
    val favFlight6 = FavoriteFlight(6, airportD.iataCode, airportE.iataCode)
    val testFavoriteFlights = mutableListOf(favFlight1, favFlight2, favFlight4)
}

object FakeFlightDatasource {
    val flight1 = Flight(airportA.iataCode, airportA.name, airportA.passengers, airportB.iataCode, airportB.name)
    val flight2 = Flight(airportC.iataCode, airportC.name, airportC.passengers, airportA.iataCode, airportA.name)
    val flight4 = Flight(airportE.iataCode, airportE.name, airportE.passengers, airportF.iataCode, airportF.name)
    val flight5 = Flight(airportA.iataCode, airportA.name, airportA.passengers, airportC.iataCode, airportC.name)
    val flight6 = Flight(airportD.iataCode, airportD.name, airportD.passengers, airportE.iataCode, airportE.name)
    val flight7 = Flight(airportB.iataCode, airportB.name, airportB.passengers, airportE.iataCode, airportE.name)
    val flight8 = Flight(airportA.iataCode, airportA.name, airportA.passengers, airportD.iataCode, airportD.name)
    val flight9 = Flight(airportA.iataCode, airportA.name, airportA.passengers, airportE.iataCode, airportE.name)
    val flight10 = Flight(airportA.iataCode, airportA.name, airportA.passengers, airportF.iataCode, airportF.name)
    val flight11 = Flight(airportC.iataCode, airportC.name, airportC.passengers, airportB.iataCode, airportB.name)
    val flight12 = Flight(airportC.iataCode, airportC.name, airportC.passengers, airportD.iataCode, airportD.name)
    val flight13 = Flight(airportC.iataCode, airportC.name, airportC.passengers, airportE.iataCode, airportE.name)
    val flight14 = Flight(airportC.iataCode, airportC.name, airportC.passengers, airportF.iataCode, airportF.name)
    val testFlights: List<Flight> = listOf(
        flight1, flight2, flight4, flight5, flight6, flight7, flight8,
        flight9, flight10, flight11, flight12, flight13, flight14,
    )

    // This is the collection of total flights departing from airportA and airportC
    val testTwoFlights = listOf(
        flight1, flight5, flight8, flight9, flight10,
        flight2, flight11, flight12, flight13, flight14
    )
}
