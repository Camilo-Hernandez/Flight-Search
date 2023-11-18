package com.camihruiz24.flight_search.data.model

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.toCompleteFlightsList
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AirportTest {

    private val repository: AirportsRepository = FakeAirportsRepository()

    private lateinit var completeAirportList: List<Airport>

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() = runBlocking(UnconfinedTestDispatcher()) {
        completeAirportList = repository.getAirportsResultsBySearch("").first()
    }

    @Test
    fun `test Airport init restriction - IATA code must have length of 3`() {
        assertFailsWith<IllegalArgumentException> {
            Airport(1, "Salamanca Airport", "COD1", 1234)
        }
    }

    @Test
    fun `test Airport init restriction - IATA code must be uppercase`() {
        assertFailsWith<IllegalArgumentException> {
            Airport(1, "Salamanca Airport", "Cod", 1234)
        }
    }

    @Test
    fun `toCompleteFlightsList - the complete list of all possible flights from given airports is done by extension function`() {
        assertEquals(
            FakeFlightDatasource.testFlightsFromAAndC,
            listOf(
                FakeAirportDatasource.airportA,
                FakeAirportDatasource.airportC,
            ).toCompleteFlightsList(completeAirportList, FakeFavoriteFlightDatasource.testSomeFavoriteFlights)
        )
    }

}
