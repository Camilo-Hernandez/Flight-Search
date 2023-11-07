package com.camihruiz24.flight_search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.toFlightList
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FlightSearchViewModel @Inject internal constructor(
    private val savedStateHandle: SavedStateHandle,
    private val flightsRepository: FlightsRepository,
    private val airportsRepository: AirportsRepository,
) : ViewModel() {
    private lateinit var completeAirportList: List<Airport>
    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    private val favoriteFlightsFlow = flightsRepository.getFavoriteFlights()

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
                val airportsResultsBySearch: Flow<List<Airport>> =
                    airportsRepository.getAirportsResultsBySearch(query)
                val flightsResults: List<Flight> =
                    flightsRepository.getAllPossibleFlightsFromEachAirport(
                        airportsFlow = airportsResultsBySearch,
                        completeAirportList = completeAirportList,
                        favoriteFlights = favoriteFlightsFlow.first(),
                    )
                        .first()
                flowOf(
                    FlightSearchUiState(
                        searchInput = query,
                        flights = flightsResults
                    )
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FlightSearchUiState(),
        )

    init {
        viewModelScope.launch {
            completeAirportList = airportsRepository.getAirportsResultsBySearch("").first()
        }
    }

    fun onSearchQueryChanged(searchQuery: String) {
        savedStateHandle[SEARCH_QUERY] = searchQuery
    }

    fun onFavoriteFlightChanged(flight: Flight) {
        viewModelScope.launch {
            with(flightsRepository) {
                if (flight.isFavorite) removeFlightFromFavorites(flight.toDBModel())
                else addFlightToFavorites(flight.toDBModel())
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
