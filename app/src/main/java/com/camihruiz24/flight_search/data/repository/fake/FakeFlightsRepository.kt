package com.camihruiz24.flight_search.data.repository.fake

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.database.toCompleteFlightsList
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.testSomeFavoriteFlights
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FakeFlightsRepository @Inject constructor() : FlightsRepository {
    override fun getAllPossibleFlightsFromAirports(
        airportsFlow: Flow<List<Airport>>,
        completeAirportList: List<Airport>,
    ): Flow<List<Flight>> =
        airportsFlow.map { airportsList ->
            airportsList.toCompleteFlightsList(completeAirportList)
        }

    override fun getFavoriteFlights(): Flow<List<FavoriteFlight>> {
        return flowOf(testSomeFavoriteFlights)
    }

    override suspend fun addFlightToFavorites(flight: FavoriteFlight) {
        if (testSomeFavoriteFlights.all { it.id != flight.id }) testSomeFavoriteFlights.add(flight)
    }

    override suspend fun removeFlightFromFavorites(flight: FavoriteFlight) {
        testSomeFavoriteFlights.remove(flight)
    }
}
