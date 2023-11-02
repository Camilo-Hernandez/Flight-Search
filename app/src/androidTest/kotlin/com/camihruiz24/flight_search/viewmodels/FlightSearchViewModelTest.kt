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
import kotlinx.coroutines.flow.take
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
import javax.inject.Inject
import kotlin.test.Test
import kotlin.test.assertEquals

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

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val savedStateHandle = SavedStateHandle()
        viewModel = FlightSearchViewModel(savedStateHandle, flightsRepository, airportsRepository)
    }

    @After
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
                    flights = FakeFlightDatasource.testTwoFlights,
                    searchInput = searchQuery
                ),
                actual = it
            )
        }
        /* Both of the previous forms cannot be done at the same time because .first() cancels the flow after
        collecting the element producing error trying to collect the flow again in the next line */
    }
}
