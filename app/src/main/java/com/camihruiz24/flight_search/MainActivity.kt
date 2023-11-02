package com.camihruiz24.flight_search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.camihruiz24.flight_search.ui.FlightSearchApp
import com.camihruiz24.flight_search.ui.theme.FlightSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchTheme {
                    FlightSearchApp()
            }
        }
    }
}
