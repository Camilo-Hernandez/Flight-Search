package com.camihruiz24.flight_search.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.camihruiz24.flight_search.R
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import com.camihruiz24.flight_search.ui.theme.FlightSearchTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchApp(
    flights: List<Flight>,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onFavoriteChanged: (Flight) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorStops: List<Pair<Float, Color>> = listOf(
        0.1f to MaterialTheme.colorScheme.primary,
        0.5f to MaterialTheme.colorScheme.secondaryContainer,
        1f to MaterialTheme.colorScheme.tertiaryContainer
    )
    val customBrush = remember {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    colors = colorStops.map { it.second },
                    from = Offset(size.width, 0f),
                    to = Offset(0f, size.height),
                    colorStops = colorStops.map { it.first }
                )
            }
        }
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    val onShowSnackbar: (Flight) -> Unit = { flight ->
        coroutineScope.launch {
            val snackbarResult: SnackbarResult = snackbarHostState.showSnackbar(
                message = "Flight removed from favorites",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> Log.d("Snackbar", "Snackbar dismissed")
                SnackbarResult.ActionPerformed -> onFavoriteChanged(flight.copy(isFavorite = false))
            }
        }
    }


    Scaffold(modifier = Modifier, snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            Modifier
                .background(customBrush)
                .padding(paddingValues)) {
            Column {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    SearchToolbar(
                        onSearchQueryChanged = onSearchQueryChanged,
                        searchQuery = searchQuery,
                        onSearchTriggered = {
                            onSearchQueryChanged(it)
                        },
                    )
                    if (searchQuery.isNotEmpty())
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { }
                        ) {
                            flights.forEach {
                                DropdownMenuItem(
                                    text = { Text("${it.departureCode} | ${it.departureName}") },
                                    onClick = {
                                        onSearchQueryChanged(it.departureName)
                                        expanded = false
                                    }
                                )
                            }
                        }
                }
                ListView(
                    modifier = modifier.padding(dimensionResource(id = R.dimen.padding_very_high)),
                    flights = flights,
                    onFavoriteChanged = onFavoriteChanged,
                    onShowSnackbar = onShowSnackbar,
                )
            }
        }
    }
}

@Composable
private fun ListView(
    modifier: Modifier,
    flights: List<Flight>,
    onFavoriteChanged: (Flight) -> Unit,
    onShowSnackbar: (Flight) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.flight_results, flights.size),
            Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_high))
        )
        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_very_high))
        ) {
            items(flights) { flight ->
                FlightCard(flight = flight, onFavoriteChanged = {
                    onFavoriteChanged(it)
                    if (it.isFavorite) onShowSnackbar(it)
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightSearchAppPreview() {
    val previewFlights: List<Flight> = FakeFlightDatasource.testFlights
    FlightSearchTheme {
        FlightSearchApp(
            flights = previewFlights,
            onSearchQueryChanged = {},
            searchQuery = "OPO",
            onFavoriteChanged = {},
        )
    }
}
