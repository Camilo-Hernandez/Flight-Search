package com.camihruiz24.flight_search.viewmodels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.camihruiz24.flight_search.MainCoroutineRule
import com.camihruiz24.flight_search.data.database.AppDatabase
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import com.camihruiz24.flight_search.runBlockingTest
import com.camihruiz24.flight_search.ui.FlightSearchUiState
import com.camihruiz24.flight_search.ui.FlightSearchViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@HiltAndroidTest
class FlightSearchViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: FlightSearchViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule: RuleChain =
        RuleChain.outerRule(hiltRule).around(instantTaskExecutorRule).around(coroutineRule)

    @Inject
    lateinit var airportsRepository: AirportsRepository

    @Inject
    lateinit var flightsRepository: FlightsRepository

    @BeforeTest
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val savedStateHandle = SavedStateHandle()
        viewModel = FlightSearchViewModel(savedStateHandle, flightsRepository, airportsRepository)
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
        // Given a search query
        val searchQuery = "OPO"
        // When the query is updated
        viewModel.onSearchQueryChanged(searchQuery)
        // Check the savedStateHandle[SEARCH_QUERY] is equal to the searchQuery
        assertEquals("OPO", viewModel.searchQuery.value)
    }

    @Test
    fun test_onSearchQueryChanged_changesTheState() = coroutineRule.runBlockingTest {
        // Given a search query
        val searchQuery = "OPO"
        // When the query is updated
        viewModel.onSearchQueryChanged(searchQuery)
        /* Drop the first emitted value of the flow (the default state) before collecting it
         to calculate the assertion (when the state is updated)
         Then check the state is updated */
        // First form
//        assertEquals(searchQuery, viewModel.uiState.drop(1).first().searchInput)
        // Second form
        viewModel.uiState.drop(1).take(1).collectLatest {
            Log.d("UI State Flow", it.searchInput)
            assertEquals(
                expected = FlightSearchUiState(
                    flights = FakeFlightDatasource.testFlightsFromAAndC,
                    searchInput = searchQuery
                ),
                actual = it
            )
        }
        /* Both of the previous forms cannot be done at the same time because .first() cancels the flow after
        collecting the element producing error trying to collect the flow again in the next line */
    }

    @Test
    fun test_onFavoriteFlightChanged_pathAddToFavorites() = runTest {
        // Given a non favorite flight
        val expectedFlight = FakeFlightDatasource.flight4
        // When calling the onFavoriteFlightChanged() view model method
        viewModel.onFavoriteFlightChanged(expectedFlight)
        // Then the uiState contains the flight as a favorite when the search query is empty
        val actualFlightList = viewModel.uiState.drop(1).first().flights
        Log.d("test onFavoriteFlightChanged pathAddToFavorites", "$actualFlightList")
        assertEquals(expectedFlight.copy(isFavorite = true), actualFlightList.last())
    }

    @Test
    fun test_onFavoriteFlightChanged_pathAddToFavoritesWhenSearching() = runTest {
        // Given a search query
        val searchQuery = "OPO"
        // When the query is updated
        viewModel.onSearchQueryChanged(searchQuery)
        /* Drop the first emitted value of the flow (the default state) before collecting it
         to calculate the assertion (when the state is updated)
         Then check the state is updated and the flight is on the current list of flights */
        viewModel.uiState.drop(1).take(2).collectLatest {
            Log.d("UI State Flow", it.flights.toString())
            assertEquals(
                expected = FakeFlightDatasource.testFlightsFromAAndC,
                actual = it.flights
            )
            /** Given a non favorite flight in the [FakeFlightDatasource.testFlightsFromAAndC] list */
            val expectedFlight = FakeFlightDatasource.flight5
            // When calling the onFavoriteFlightChanged() view model method
            viewModel.onFavoriteFlightChanged(expectedFlight)
            // Then the uiState contains the flight as a favorite when the search query is empty
            val actualFlightList = it.flights
            Log.d("test onFavoriteFlightChanged pathAddToFavorites", "$actualFlightList")
            // TODO: Make this test pass by refactoring the updating flow of uiState
            assertEquals(expectedFlight.copy(isFavorite = true), actualFlightList.component2())
        }
    }

    @Test
    fun test_onFavoriteFlightChanged_pathRemoveFromFavorites() = runTest {
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
