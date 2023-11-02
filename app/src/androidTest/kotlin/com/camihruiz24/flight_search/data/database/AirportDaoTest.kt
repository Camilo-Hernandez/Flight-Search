package com.camihruiz24.flight_search.data.database

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportA
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportB
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportC
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportD
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportE
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportF
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.testAirports
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class AirportDaoTest {

    private lateinit var airportDao: AirportDao
    private lateinit var fakeAppDatabase: AppDatabase

    @BeforeTest
    fun setUp() = runBlocking {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        fakeAppDatabase = Room.inMemoryDatabaseBuilder(targetContext, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()

        airportDao = fakeAppDatabase.getAirportDao()

        airportDao.insertAll(testAirports)

    }

    @AfterTest
    fun closeDb() {
        fakeAppDatabase.close()
    }

    @Test
    fun daoSearchQuery_getAllTheAirportsByQueryingWithEmptyString() = runTest {
        // Given the airport search query
        val searchQuery = ""
        // When the DAO emits the results of the search
        val search: List<Airport> = airportDao.getAirportsBySearch(searchQuery).first()
        Log.d("Search Query", "$search")
        // Then check the search is done correctly.
        assertIs<List<Airport>>(search)
        assertNotNull(search)
        assertTrue(search.isNotEmpty())
        assertEquals(6, search.size)
        assertTrue(search.all { it in testAirports })
    }

    @Test
    fun daoSearchQuery_getTheCompleteListOfAirportsByQueryingWithAirport() = runTest {
        // Given the airport search query
        val searchQuery = "Airport"
        // When the DAO emits the results of the search
        val search: List<Airport> = airportDao.getAirportsBySearch(searchQuery).first()
        Log.d("Search Query", "$search")
        // Then check the search is done correctly. All of the 5 airports satisfy the search query "Airport"
        assertIs<List<Airport>>(search)
        assertNotNull(search)
        assertTrue(search.isNotEmpty())
        assertEquals(6, search.size)
        assertTrue(search.all { it in testAirports })
    }

    @Test
    fun daoSearchQuery_getASublistOfAirportsWhenIntroducingASearchQuery() = runTest {

        // Given a search query
        val searchQuery = "OPO"

        // When the DAO emits the results of the search
        val search: List<Airport> = airportDao.getAirportsBySearch(searchQuery).first()
        Log.d("Search Query", search.toString())

        // Then check the search is done correctly.
        // Only 2 airports satisfy the search query "OPO". One by iata_code and the another by name
        assertIs<List<Airport>>(search)
        assertNotNull(search)
        assertTrue(search.isNotEmpty())
        assertEquals(2, search.size)
        assertTrue(search.containsAll(listOf(airportA, airportC)))
        // B is not included despite having "To po"
        assertFalse(search.containsAll(listOf(airportB, airportD, airportE, airportF)))
    }

    @Test
    fun daoSearchQuery_getZeroAirportsWhenIntroducingASearchQuery() = runTest {

        // Given a search query
        val searchQuery = "GokuSan"

        // When the DAO emits the results of the search
        val search: List<Airport> = airportDao.getAirportsBySearch(searchQuery).first()
        Log.d("Search Query", search.toString())

        // Then check the search is done correctly. Only 2 airports satisfy the search query "OPO"
        assertIs<List<Airport>>(search)
        assertTrue(search.isEmpty())
        assertEquals(0, search.size)
        assertFalse(search.any { it in (listOf(airportA, airportB, airportC, airportD, airportE)) })

    }

}
