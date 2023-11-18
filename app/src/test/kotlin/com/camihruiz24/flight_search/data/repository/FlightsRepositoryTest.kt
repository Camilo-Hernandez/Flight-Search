package com.camihruiz24.flight_search.data.repository

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.testAirports
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.favFlight3
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.favFlight5
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.testSomeFavoriteFlights
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource.testFlights
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource.testFlightsFromAAndC
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FlightsRepositoryTest {

    private lateinit var flightsRepository: FlightsRepository
    private lateinit var airportsRepository: AirportsRepository
    private lateinit var completeAirportList: List<Airport>

    @BeforeTest
    fun setUp() = runBlocking {
        flightsRepository = FakeFlightsRepository()
        airportsRepository = FakeAirportsRepository()
        // And given the complete list of airports available in the datasource
        completeAirportList = airportsRepository.getAirportsResultsBySearch("").first()
    }

    @Test
    fun test_getAllPossibleFlightsFromAirports_getFlightsFromTwoAirports() = runTest {
        // Given a list of 2 airports
        val airportList = listOf(
            FakeAirportDatasource.airportA, // iata_code = "OPO"
            FakeAirportDatasource.airportC, // name = "Topo Airport"
        )
        // When the repository receives the flow and produces a list of all possible flights
        val flightsList: List<Flight> =
            flightsRepository.run {
                getAllPossibleFlightsFromEachAirport(
                    airportList,
                    completeAirportList,
                    this.getFavoriteFlights().first()
                )
            }
        /**
         * Check the result's size is equal to (n-1)*s, where
         * * n = total number of airports
         * * s = number of airports in the list flow
         */
        assertEquals((testAirports.size - 1) * airportList.size, flightsList.size)
        // Then check all the flights in the test flights list are contained in the result
        assertEquals(testFlightsFromAAndC, flightsList)
    }

    @Test
    fun test_getAllPossibleFlightsFromAirports() = runTest {
        // Given a list of airports
        testAirports
        // When the repository receives the flow of f and produces a list of all possible flights
        val flightList: List<Flight> =
            flightsRepository.run {
                getAllPossibleFlightsFromEachAirport(
                    testAirports,
                    completeAirportList,
                    this.getFavoriteFlights().first()
                )
            }
        // Then check all the flights in the test flights list are contained in the result
        assertTrue(testFlights.all { it in flightList })
        /**
         * And check the result's size is equal to (n-1)*s, where
         * * n = total number of airports
         * * s = number of airports in the list flow
         */
        assertEquals((testAirports.size - 1) * testAirports.size, flightList.size)
    }

    @Test
    fun test_getAllPossibleFlightsFromAirports_getNotASingleAirport() = runTest {
        // Given an empty list of airports
        // When the repository receives the flow and produces a flow of a list of all possible flights
        val flightsList: List<Flight> =
            flightsRepository.run {
                getAllPossibleFlightsFromEachAirport(
                    emptyList<Airport>(),
                    completeAirportList,
                    this.getFavoriteFlights().first()
                )
            }
        // Then check non of the flights in the test flights list are contained in the result
        assertFalse(testFlights.any { it in flightsList })
        // And check the result's size is equal to zero
        assertEquals(0, flightsList.size)
    }

    @Test
    fun test_getFavoriteFlights() = runTest {

        // When the repository emits a value
        val firstItem =
            flightsRepository.getFavoriteFlights().first() // Returns the first item in the flow

        // Then check it's the expected item
        assertEquals(testSomeFavoriteFlights, firstItem)
    }

    @Test
    fun test_addFlightToFavorites() = runTest {
        // Given a flight
        val flight = favFlight3
        // When the repository adds it to favorite flights
        flightsRepository.addFlightToFavorites(flight)
        // Then check the flight has been added
        assertContains(testSomeFavoriteFlights, flight)
    }

    @Test
    fun test_removeFlightFromFavorites() = runTest {
        // Given a flight
        val flight = favFlight5
        // When the repository adds it to favorite flights
        flightsRepository.removeFlightFromFavorites(flight)
        // Then check the flight has been added
        assertFalse(testSomeFavoriteFlights.contains(flight))
    }
}
