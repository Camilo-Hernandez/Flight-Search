package com.camihruiz24.flight_search.data.database

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportA
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportE
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.testFavoriteFlights
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class FavoriteFlightDaoTest {

    private lateinit var favoriteFlightDao: FavoriteFlightDao
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

        favoriteFlightDao = fakeAppDatabase.getFavoriteFlightDao()

        // Added in wrong order to test ordering
        favoriteFlightDao.addFlightToFavorites(FakeFavoriteFlightDatasource.favFlight2)
        favoriteFlightDao.addFlightToFavorites(FakeFavoriteFlightDatasource.favFlight4)
        favoriteFlightDao.addFlightToFavorites(FakeFavoriteFlightDatasource.favFlight1)
    }

    @AfterTest
    fun closeDb() {
        fakeAppDatabase.close()
    }


    @Test
    fun test_getFavoriteFlights() = runTest {
        // When the DAO emits the results of the query
        val search: List<FavoriteFlight> = favoriteFlightDao.getFavoriteFlights().first()
        Log.d("Search Query", "$search")
        // Then check the query is done correctly.
        assertIs<List<FavoriteFlight>>(search)
        assertNotNull(search)
        assertTrue(search.isNotEmpty())
        assertEquals(3, search.size) // I added 3 in the setUp
        assertTrue(search.all { it in testFavoriteFlights })
    }

    @Test
    fun test_addFlightToFavorites() = runTest {
        // Given the flight
        val favFlight = FavoriteFlight(3, airportE.iataCode, airportA.iataCode)
        // When the DAO inserts the flight in the database
        favoriteFlightDao.addFlightToFavorites(favFlight)
        // Then check the insert is done correctly.
        val currentFlights: List<FavoriteFlight> = favoriteFlightDao.getFavoriteFlights().first()
        assertTrue(favFlight in currentFlights)
        // The favorite flight is put in order by primary key
        assertEquals(FakeFavoriteFlightDatasource.favFlight1, currentFlights[0])
        assertEquals(FakeFavoriteFlightDatasource.favFlight2, currentFlights[1])
        assertEquals(favFlight, currentFlights[2])
        assertEquals(FakeFavoriteFlightDatasource.favFlight4, currentFlights[3])

        assertTrue(currentFlights.all {
            it in listOf(
                FakeFavoriteFlightDatasource.favFlight1,
                FakeFavoriteFlightDatasource.favFlight2,
                FakeFavoriteFlightDatasource.favFlight4,
                favFlight
            )
        })

    }

    @Test
    fun test_removeFlightFromFavorites() = runTest {

        // When the DAO deletes a flight
        favoriteFlightDao.removeFlightFromFavorites(FakeFavoriteFlightDatasource.favFlight2)

        // Then check the delete is done correctly.
        val currentFlights: List<FavoriteFlight> = favoriteFlightDao.getFavoriteFlights().first()
        assertEquals(2, currentFlights.size)
        assertEquals(
            listOf(
                FakeFavoriteFlightDatasource.favFlight1,
                FakeFavoriteFlightDatasource.favFlight4
            ), currentFlights
        )
    }


}
