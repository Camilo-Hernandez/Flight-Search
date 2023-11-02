package com.camihruiz24.flight_search.data.repository

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.testAirports
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AirportsRepositoryTest {

    private lateinit var repository: AirportsRepository

    @BeforeTest
    fun setUp() {
        repository = FakeAirportsRepository()
    }

    @Test
    fun test_getAirportsResultsBySearch() = runTest {
        // Given a search query
        val search = "OPO"
        // When the repository emits a value
        val searchResults: List<Airport> =
            repository.getAirportsResultsBySearch(search).first() // Returns the first item in the flow
        // Then check it's the expected item
        assertEquals(listOf(testAirports[0], testAirports[2]), searchResults)
    }

    @Test
    fun test_getNotASingleResultWhenSearching() = runTest {
        // Given a search query
        val search = "Oklahoma"
        // When the repository emits a value
        val searchResults: List<Airport> =
            repository.getAirportsResultsBySearch(search).first() // Returns the first item in the flow
        // Then check it's the expected item
        assertEquals(emptyList<Airport>(), searchResults)
    }

    @Test
    fun test_theCompleteListOfAirportsWhenSearching() = runTest {
        // Given a search query
        val search = ""
        // When the repository emits a value
        val searchResults: List<Airport> =
            repository.getAirportsResultsBySearch(search).first() // Returns the first item in the flow
        // Then check it's the expected item
        assertEquals(testAirports, searchResults)
    }

}

