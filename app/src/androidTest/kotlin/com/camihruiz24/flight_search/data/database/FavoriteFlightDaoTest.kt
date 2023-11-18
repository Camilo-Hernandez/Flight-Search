package com.camihruiz24.flight_search.data.database

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportA
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportDatasource.airportE
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.testSomeFavoriteFlights
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
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

        /** Ordering does not matter due to the primary key of the [FavoriteFlight] class not being created
         * deterministically */
        favoriteFlightDao.addFlightToFavorites(FakeFavoriteFlightDatasource.favFlight2)
        favoriteFlightDao.addFlightToFavorites(FakeFavoriteFlightDatasource.favFlight1)
        favoriteFlightDao.addFlightToFavorites(FakeFavoriteFlightDatasource.favFlight3)
    }

    @AfterTest
    fun closeDb() {
        fakeAppDatabase.close()
    }

    @Test
    fun test_getFavoriteFlights() = runTest {
        // When the DAO emits the results of the query
        val actualFavoriteFlights: List<FavoriteFlight> = favoriteFlightDao.getFavoriteFlights().first()
        Log.d("Search Query", "$actualFavoriteFlights")
        // Then check the query is done correctly.
        assertIs<List<FavoriteFlight>>(actualFavoriteFlights)
        assertNotNull(actualFavoriteFlights)
        assertTrue(actualFavoriteFlights.isNotEmpty())
        assertEquals(testSomeFavoriteFlights.size, actualFavoriteFlights.size)
        assertTrue(actualFavoriteFlights.all { it in testSomeFavoriteFlights })
    }

    @Test
    fun test_addFlightToFavorites() = runTest {
        // Given the flight
        val favFlight = FavoriteFlight(airportE.iataCode, airportA.iataCode)
        // When the DAO inserts the flight in the database
        favoriteFlightDao.addFlightToFavorites(favFlight)
        // Then check the insert is done correctly.
        val currentFlights: List<FavoriteFlight> = favoriteFlightDao.getFavoriteFlights().first()
        assertTrue(favFlight in currentFlights)
        assertContains(currentFlights, FakeFavoriteFlightDatasource.favFlight1)
        assertContains(currentFlights, FakeFavoriteFlightDatasource.favFlight2)
        assertContains(currentFlights, FakeFavoriteFlightDatasource.favFlight3)
        assertContains(currentFlights, favFlight)

        assertTrue(currentFlights.all {
            it in listOf(
                FakeFavoriteFlightDatasource.favFlight1,
                FakeFavoriteFlightDatasource.favFlight2,
                FakeFavoriteFlightDatasource.favFlight3,
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
        assertEquals(testSomeFavoriteFlights.size-1, currentFlights.size)
        assertEquals(
            /** Given that the primary key of [FavoriteFlight] is non deterministic, it is the best option
             * to just test for existence rather than existence AND order of the elements in the DB */
            setOf(
                FakeFavoriteFlightDatasource.favFlight1,
                FakeFavoriteFlightDatasource.favFlight3,
            ), currentFlights.toSet()
        )
    }


}
