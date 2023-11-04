package com.camihruiz24.flight_search.ui

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.camihruiz24.flight_search.data.model.Flight

@Composable
fun FlightCard(flight: Flight, modifier: Modifier = Modifier) {
    Card(
        modifier,
        elevation = CardDefaults.cardElevation(2.dp),
        shape = MaterialTheme.shapes.large
    ) {

    }
}

@Preview
@Composable
fun FlightCardPreview() {
    val flight = Flight(
        departureCode = "OPO",
        departureName = "Topo Airport",
        numberOfPassengers = 30,
        destinationCode = "OCK",
        destinationName = "Ocklahoma",
    )
    FlightCard(
        flight = flight
    )
}
