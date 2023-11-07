package com.camihruiz24.flight_search.data.model

import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.database.toFlightList
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FlightTest {

    private val fakeAirportsRepository = FakeAirportsRepository()

    @Test
    fun `test Flight init restriction - departure Code must be different than destination Code`() {
        assertFailsWith<IllegalArgumentException> {
            Flight("Name 1", "COD", 3000, "Name 2", "COD", 1230)
        }
    }

    @Test
    fun `test Flight init restriction - departure Code AND destination Code must have length of 3`() {
        assertFailsWith<IllegalArgumentException> {
            Flight("Name 1", "COD", 3000, "Name 2", "COD2", 1230)
        }
        assertFailsWith<IllegalArgumentException> {
            Flight("Name 1", "COD1", 3000, "Name 2", "COD", 1230)
        }
    }

    @Test
    fun `test toFlightList - Conversion from List of FavoriteFlight to List of Flight is done by extension function`() =
        runTest {
            // Given a list of favorite flights
            val favFlights: List<FavoriteFlight> =
                FakeFavoriteFlightDatasource.testSomeFavoriteFlights
            // When converting it to list of flights
            val flights: List<Flight> = favFlights.toFlightList(fakeAirportsRepository)
            // Then check the conversion in done correctly
            assertIs<List<Flight>>(flights)
            assertEquals(
                expected = listOf(
                    FakeFlightDatasource.flight1,
                    FakeFlightDatasource.flight2,
                    FakeFlightDatasource.flight3,
                ),
                actual = flights
            )
        }

    @Test
    fun `test toDBModel - Five conversions from Flight to FavoriteFlight is done by extension function`() {
        val fiveFlights: List<Flight> = FakeFlightDatasource.testFlights.take(5)
        val testFavFlights = FakeFavoriteFlightDatasource.testAllFavoriteFlights

        val favFlights: List<FavoriteFlight> = fiveFlights.map {
            it.toDBModel()
        }

        assertEquals(testFavFlights.take(5), favFlights)
        assertTrue(testFavFlights.all { it in favFlights })
    }

    @Test
    fun `test toDBModel - Conversions from Flight to FavoriteFlight is done by extension function`() {
        val flights: List<Flight> = FakeFlightDatasource.testFlights
        val testFavFlights = FakeFavoriteFlightDatasource.testAllFavoriteFlights

        val favFlights: List<FavoriteFlight> = flights.map {
            it.toDBModel()
        }
        assertTrue(testFavFlights.all { it in favFlights })
    }

}
