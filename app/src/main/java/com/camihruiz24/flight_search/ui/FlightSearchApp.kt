package com.camihruiz24.flight_search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.camihruiz24.flight_search.R
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import com.camihruiz24.flight_search.ui.theme.FlightSearchTheme

@Composable
fun FlightSearchApp(
    flights: List<Flight>,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onFavoriteChanged: (Flight) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        SearchToolbar(
            onSearchQueryChanged = onSearchQueryChanged,
            searchQuery = searchQuery,
            onSearchTriggered = onSearchQueryChanged
        )
        ListView(
            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_very_high)),
            flights = flights,
            onFavoriteChanged = onFavoriteChanged,
        )
    }
}

@Composable
private fun ListView(
    modifier: Modifier,
    flights: List<Flight>,
    onFavoriteChanged: (Flight) -> Unit,
) {
    Column(modifier = modifier) {
        Text(text = stringResource(R.string.flight_results, flights.size), Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_high)))
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_very_high))) {
            items(flights) {
                FlightCard(flight = it, onFavoriteChanged = onFavoriteChanged)
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
            onFavoriteChanged = {}
        )
    }
}