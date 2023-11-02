package com.camihruiz24.flight_search.data.model

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.toCompleteFlightsList
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class AirportTest {

    val repository: AirportsRepository = FakeAirportsRepository()

    private lateinit var completeAirportList: List<Airport>

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() = runBlocking(UnconfinedTestDispatcher()) {
        completeAirportList = repository.getAirportsResultsBySearch("").first()
    }

    @Test
    fun test_toCompleteFlightsList() {
        assertEquals(
            FakeFlightDatasource.testTwoFlights,
            listOf(
                FakeAirportDatasource.airportA,
                FakeAirportDatasource.airportC,
            ).toCompleteFlightsList(completeAirportList)
        )
    }

}
