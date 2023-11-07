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
    val favFlight1 = FavoriteFlight(airportA.iataCode, airportB.iataCode)
    val favFlight2 = FavoriteFlight(airportC.iataCode, airportA.iataCode)
    val favFlight3 = FavoriteFlight(airportD.iataCode, airportE.iataCode)
    val favFlight4 = FavoriteFlight(airportE.iataCode, airportF.iataCode)
    val favFlight5 = FavoriteFlight(airportA.iataCode, airportC.iataCode)
    val testSomeFavoriteFlights = mutableListOf(favFlight1, favFlight2, favFlight3)
    val testAllFavoriteFlights = listOf(favFlight1, favFlight2, favFlight3, favFlight4, favFlight5, )
}

object FakeFlightDatasource {
    val flight1 = Flight(airportA.name, airportA.iataCode, airportA.passengers, airportB.name, airportB.iataCode, airportB.passengers, true)
    val flight2 = Flight(airportC.name, airportC.iataCode, airportC.passengers, airportA.name, airportA.iataCode, airportA.passengers, true)
    val flight3 = Flight(airportD.name, airportD.iataCode, airportD.passengers, airportE.name, airportE.iataCode, airportE.passengers, true)
    val flight4 = Flight(airportE.name, airportE.iataCode, airportE.passengers, airportF.name, airportF.iataCode, airportF.passengers)
    val flight5 = Flight(airportA.name, airportA.iataCode, airportA.passengers, airportC.name, airportC.iataCode, airportC.passengers)
    val flight6 = Flight(airportC.name, airportC.iataCode, airportC.passengers, airportB.name, airportB.iataCode, airportB.passengers)
    val flight7 = Flight(airportB.name, airportB.iataCode, airportB.passengers, airportE.name, airportE.iataCode, airportE.passengers)
    val flight8 = Flight(airportA.name, airportA.iataCode, airportA.passengers, airportD.name, airportD.iataCode, airportD.passengers)
    val flight9 = Flight(airportA.name, airportA.iataCode, airportA.passengers, airportE.name, airportE.iataCode, airportE.passengers)
    val flight10 = Flight(airportA.name, airportA.iataCode, airportA.passengers, airportF.name, airportF.iataCode, airportF.passengers)
    val flight11 = Flight(airportC.name, airportC.iataCode, airportC.passengers, airportD.name, airportD.iataCode, airportD.passengers)
    val flight12 = Flight(airportC.name, airportC.iataCode, airportC.passengers, airportE.name, airportE.iataCode, airportE.passengers)
    val flight13 = Flight(airportC.name, airportC.iataCode, airportC.passengers, airportF.name, airportF.iataCode, airportF.passengers)
    val testFlights: List<Flight> = listOf(
        flight1, flight2, flight3, flight4, flight5, flight6, flight7, flight8,
        flight9, flight10, flight11, flight12, flight13,
    )

    // This is the collection of total flights departing from airportA and airportC
    val testFlightsFromAAndC = listOf(
        flight1, flight5, flight8, flight9, flight10, // departing from airportA, in order of destinations (from B to F)
        flight2, flight6, flight11, flight12, flight13, // departing from airportC, in order of destinations
    )
}
