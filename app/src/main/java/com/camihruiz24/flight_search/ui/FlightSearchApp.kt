package com.camihruiz24.flight_search.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.camihruiz24.flight_search.ui.theme.FlightSearchTheme


@Composable
fun FlightSearchApp(
    modifier: Modifier = Modifier,
    viewModel: FlightSearchViewModel = hiltViewModel(),
) {
    // TODO: Implementation
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlightSearchTheme {
        FlightSearchApp()
    }
}