package com.camihruiz24.flight_search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.camihruiz24.flight_search.ui.FlightSearchApp
import com.camihruiz24.flight_search.ui.FlightSearchViewModel
import com.camihruiz24.flight_search.ui.theme.FlightSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchTheme {
                val viewModel = hiltViewModel<FlightSearchViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val onSearchQueryChanged = viewModel::onSearchQueryChanged
                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val onFavoriteFlightChanged = viewModel::onFavoriteFlightChanged
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlightSearchApp(
                        flights = uiState.flights,
                        onSearchQueryChanged = onSearchQueryChanged,
                        searchQuery = searchQuery,
                        onFavoriteChanged = onFavoriteFlightChanged
                    )
                }
            }
        }
    }
}
