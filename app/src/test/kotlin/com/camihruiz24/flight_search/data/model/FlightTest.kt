package com.camihruiz24.flight_search.data.model

import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.database.toFlightList
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FlightTest {

    private val fakeAirportsRepository = FakeAirportsRepository()

    @Test
    fun `test toFlightList - Conversion from List of FavoriteFlight to List of Flight is done by extension function`() =
        runTest {
            // Given a list of favorite flights
            val favFlights: List<FavoriteFlight> =
                FakeFavoriteFlightDatasource.testFavoriteFlights
            // When converting it to list of flights
            val flights: List<Flight> = favFlights.toFlightList(fakeAirportsRepository)
            // Then check the conversion in done correctly
            assertIs<List<Flight>>(flights)
            assertEquals(
                expected = listOf(
                    FakeFlightDatasource.flight1,
                    FakeFlightDatasource.flight2,
                    FakeFlightDatasource.flight4,
                ),
                actual = flights
            )
        }

    @Test
    fun `test toDBModel - Conversion from Flight to FavoriteFlight is done by extension function`() {
        val flights: List<Flight> = FakeFlightDatasource.testFlights

        val favFlights = flights.map { it.toDBModel() }
        // TODO: Complete the test
//        assertTrue(favFlights.containsAll(FakeFavoriteFlightDatasource.testFavoriteFlights))
//        assertTrue(FakeFavoriteFlightDatasource.testFavoriteFlights.all { it in favFlights })
    }

}
