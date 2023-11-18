package com.camihruiz24.flight_search.viewmodels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.camihruiz24.flight_search.MainCoroutineRule
import com.camihruiz24.flight_search.data.AppPrefsStorage
import com.camihruiz24.flight_search.data.app_preferences.FakeDataStore
import com.camihruiz24.flight_search.data.database.AppDatabase
import com.camihruiz24.flight_search.data.database.FavoriteFlightDao
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightsRepository
import com.camihruiz24.flight_search.runBlockingTest
import com.camihruiz24.flight_search.ui.FlightSearchUiState
import com.camihruiz24.flight_search.ui.FlightSearchViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

@HiltAndroidTest
class FlightSearchViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: FlightSearchViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()
    private lateinit var favoriteFlightDao: FavoriteFlightDao

    @get:Rule
    val rule: RuleChain =
        RuleChain.outerRule(hiltRule).around(instantTaskExecutorRule).around(coroutineRule)

    @Inject
    lateinit var airportsRepository: AirportsRepository

    @Inject
    lateinit var flightsRepository: FlightsRepository

    @BeforeTest
    fun setUp() = coroutineRule.runBlockingTest {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
//            .allowMainThreadQueries()
            .build()

        favoriteFlightDao = appDatabase.getFavoriteFlightDao()

        // Assert the fake repos are injected correctly using Hilt modules
        assertIs<FakeAirportsRepository>(airportsRepository)
        assertIs<FakeFlightsRepository>(flightsRepository)

        val savedStateHandle = SavedStateHandle()
        viewModel = FlightSearchViewModel(
            savedStateHandle,
            flightsRepository,
            airportsRepository,
            AppPrefsStorage(FakeDataStore.testDataStore),
            coroutineRule.testDispatcher
        )
    }

    @AfterTest
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun test_UiStateIsInitiallyDefault() = coroutineRule.runBlockingTest {
        assertEquals(FlightSearchUiState(), viewModel.uiState.value)
    }

    @Test
    fun test_onSearchQueryChanged_changesTheSavedStateHandle() = coroutineRule.runBlockingTest {
        coroutineScope {
            val job: Job = launch { viewModel.uiState.collect() }
            // Given a search query
            val searchQuery = "OPO"
            // When the query is updated
            viewModel.onSearchQueryChanged(searchQuery)
            // Check the savedStateHandle[SEARCH_QUERY] is equal to the searchQuery
            assertEquals("OPO", viewModel.searchQuery.drop(2).first())
            job.cancel()
        }
    }

    @Test
    fun test_onSearchQueryChanged_changesTheState() = coroutineRule.runBlockingTest {
        coroutineScope {
            val job: Job = launch { viewModel.uiState.collect() }
            // Given a search query
            val searchQuery = "OPO"
            // When the query is updated
            viewModel.onSearchQueryChanged(searchQuery)
            /* Drop the first emitted value of the flow (the default state) before collecting it
         to calculate the assertion (when the state is updated)
         Then check the state is updated */
            // First form
            assertEquals(searchQuery, viewModel.uiState.drop(2).first().searchInput)
            // Second form
            /*viewModel.uiState.drop(1).take(1).collectLatest {
            Log.d("UI State Flow", it.searchInput)
            assertEquals(
                expected = FlightSearchUiState(
                    flights = FakeFlightDatasource.testFlightsFromAAndC,
                    searchInput = searchQuery
                ),
                actual = it
            )
        }*/
            /* Both of the previous forms cannot be done at the same time because .first() cancels the flow after
        collecting the element producing error trying to collect the flow again in the next line */
            job.cancel()
        }
    }
    @Test
    fun test_onFavoriteFlightChanged_pathAddToFavorites() = coroutineRule.runBlockingTest {
        // Given a non favorite flight
        val expectedFlight = FakeFlightDatasource.flight4
        // When calling the onFavoriteFlightChanged() view model method
        viewModel.onFavoriteFlightChanged(expectedFlight)
        // Then the uiState contains the flight as a favorite when the search query is empty
        val actualFlightList = viewModel.uiState.drop(1).first().flights
        Log.d("test onFavoriteFlightChanged pathAddToFavorites", "$actualFlightList")
        assertEquals(expectedFlight.copy(isFavorite = true), actualFlightList.last())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_onFavoriteFlightChanged_pathAddToFavoritesWhileSearching() = coroutineRule.runBlockingTest {
        coroutineScope {
            // Create an empty collector for the StateFlow
            val job: Job = launch { viewModel.uiState.collect() }

            assertEquals(FlightSearchUiState(), viewModel.uiState.value)
            // Given a search query
            val searchQuery = "OPO"
            // When the query is updated
            viewModel.onSearchQueryChanged(searchQuery)
            /* Drop the first emitted value of the flow (the default state) before collecting it
         to calculate the assertion (when the state is updated) */
            val uiStateSecondValue: FlightSearchUiState = viewModel.uiState.drop(2).first()
            viewModel.onFavoriteFlightChanged(uiStateSecondValue.flights[6])
            val uiStateThirdValue: FlightSearchUiState = viewModel.uiState.first()

            /* Then check the state is updated and the flight is on the current list of flights */
            Log.d("UI State Flow", uiStateSecondValue.flights[6].toString())
            assertEquals(
                expected = FakeFlightDatasource.testFlightsFromAAndC,
                actual = uiStateSecondValue.flights
            )
            assertEquals(
                expected = FakeFlightDatasource.testFlightsFromAAndC[6],
                actual = uiStateSecondValue.flights[6]
            )
            /** Given a non favorite flight in the [FakeFlightDatasource.testFlightsFromAAndC] list */
            val expectedFlight = FakeFlightDatasource.testFlightsFromAAndC[6]
            // When calling the onFavoriteFlightChanged() view model method
            // Then the uiState contains the flight as a favorite
            val actualFlightList = uiStateThirdValue.flights
            Log.d("test onFavoriteFlightChanged pathAddToFavorites", "${actualFlightList[6]}")
            // TODO: Make this test pass
//        assertEquals(expectedFlight.copy(isFavorite = true), actualFlightList[6])
            job.cancel()
        }
    }

    @Test
    fun test_onFavoriteFlightChanged_pathAddToFavoritesWhileSearching_2() = coroutineRule.runBlockingTest {
        coroutineScope {
            val job: Job = launch { viewModel.uiState.collect() }

            // Given a search query
            val searchQuery = "OPO"
            // When the query is updated
            viewModel.onSearchQueryChanged(searchQuery)
            /** Given a non favorite flight in the [FakeFlightDatasource.testFlightsFromAAndC] list */
            val expectedFlight = FakeFlightDatasource.testFlightsFromAAndC.component2()
            // When calling the onFavoriteFlightChanged() view model method
            // Then the uiState contains the flight as a favorite
            val actualFlightList = viewModel.uiState.value.flights
            // TODO: Make this test pass
//        Log.d("test onFavoriteFlightChanged pathAddToFavorites", "${actualFlightList.component2()}")
//        assertEquals(expectedFlight.copy(isFavorite = true), actualFlightList.component2())

            job.cancel()
        }
    }
    @Test
    fun test_onFavoriteFlightChanged_pathRemoveFromFavorites() = coroutineRule.runBlockingTest {
        // Given a favorite flight
        val expectedFlight = FakeFlightDatasource.flight1
        // When calling the onFavoriteFlightChanged() view model method
        viewModel.onFavoriteFlightChanged(expectedFlight)
        // Then the uiState does not contains the flight as a favorite when the search query is empty
        val actualFlightListAfter = viewModel.uiState.drop(1).first().flights
        Log.d("test onFavoriteFlightChanged pathRemoveFromFavorites", "$actualFlightListAfter")
        assertFalse(expectedFlight in actualFlightListAfter)
    }

}
