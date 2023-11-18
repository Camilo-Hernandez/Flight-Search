package com.camihruiz24.flight_search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camihruiz24.flight_search.data.PreferencesStorage
import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.database.toFlightList
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import com.camihruiz24.flight_search.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject internal constructor(
    private val savedStateHandle: SavedStateHandle,
    private val flightsRepository: FlightsRepository,
    private val airportsRepository: AirportsRepository,
    val preferencesStorage: PreferencesStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private lateinit var completeAirportList: List<Airport> // Get all the airports from the "airport" table
    private var firstTime = true
    val searchQuery: StateFlow<String> =
        /*= preferencesStorage.lastSearchQuery.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), ""
        )*/ savedStateHandle.getStateFlow<String>(key = SEARCH_QUERY, initialValue = "")
        .combine(preferencesStorage.lastSearchQuery) { currentSearchQuery: String, savedSearchQuery: String ->
            if (firstTime) {
                firstTime = false
                savedSearchQuery
            } else currentSearchQuery
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    private val favoriteFlightsFlow: Flow<List<FavoriteFlight>> = flightsRepository.getFavoriteFlights()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FlightSearchUiState> =
        searchQuery.flatMapLatest { query ->
            // If the search query is blank, show the favorite flights from the datasource
            if (query.isBlank()) {
                favoriteFlightsFlow
                    .mapLatest {
                        FlightSearchUiState(
                            flights = it.toFlightList(airportsRepository)
                        )
                    }
            } else {
                // If the Search Query is not Blank, show the flights results from the query
                airportsRepository.getAirportsResultsBySearch(query)
                    .combine(favoriteFlightsFlow) { currentAirportsList, favoriteFlights ->
                        flightsRepository.getAllPossibleFlightsFromEachAirport(
                            airports = currentAirportsList,
                            completeAirportList = completeAirportList,
                            favoriteFlights = favoriteFlights,
                        )
                            .let { flightList: List<Flight> ->
                                FlightSearchUiState(
                                    searchInput = query,
                                    flights = flightList
                                )
                            }
                    }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FlightSearchUiState(),
            )

    init {
        viewModelScope.launch {
            // Create the full list of airports available in the DB
            completeAirportList = airportsRepository.getAirportsResultsBySearch("").first()
        }
    }

    fun onSearchQueryChanged(searchQuery: String) {
        savedStateHandle[SEARCH_QUERY] = searchQuery

        // Save in preferences every time the search is saved in a save main thread manner
        viewModelScope.launch {
            preferencesStorage.setLastSearchQueryPreference(searchQuery)
        }
    }

    fun onFavoriteFlightChanged(flight: Flight) {
        viewModelScope.launch {
            with(flightsRepository) {
                if (flight.isFavorite)
                    removeFlightFromFavorites(flight.toDBModel())
                else
                    addFlightToFavorites(flight.toDBModel())
            }
        }
    }

    internal companion object {
        const val SEARCH_QUERY = "searchQuery"
    }

}

data class FlightSearchUiState(
    val flights: List<Flight> = emptyList(),
    val searchInput: String = "",
    val searchSuggestions: List<Airport> = emptyList(),
    val isSearching: Boolean = true,
)
