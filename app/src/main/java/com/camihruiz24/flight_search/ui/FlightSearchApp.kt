package com.camihruiz24.flight_search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightDatasource
import com.camihruiz24.flight_search.ui.theme.FlightSearchTheme


@Composable
fun FlightSearchApp(
    flights: List<Flight>,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier,
) {
    Column {
        SearchToolbar(
            onSearchQueryChanged = onSearchQueryChanged,
            searchQuery = searchQuery,
            onSearchTriggered = onSearchQueryChanged
        )
        ListView(modifier, flights)
    }
}

@Composable
private fun ListView(
    modifier: Modifier,
    flights: List<Flight>
) {
    Text(text = "${flights.size.toString()} flights")
    LazyColumn(modifier.fillMaxSize()) {
        items(flights) {
            Text(text = it.toString())
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
            searchQuery = "OPO"
        )
    }
}