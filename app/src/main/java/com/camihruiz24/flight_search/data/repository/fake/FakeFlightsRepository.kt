package com.camihruiz24.flight_search.data.repository.fake

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.database.toCompleteFlightsList
import com.camihruiz24.flight_search.data.model.Flight
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFavoriteFlightDatasource.testSomeFavoriteFlights
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

val testFavoriteFlights = testSomeFavoriteFlights

@Singleton
class FakeFlightsRepository @Inject constructor() : FlightsRepository {
    override fun getAllPossibleFlightsFromEachAirport(
        airports: List<Airport>,
        completeAirportList: List<Airport>,
        favoriteFlights: List<FavoriteFlight>,
    ): List<Flight> =
        airports.toCompleteFlightsList(completeAirportList, favoriteFlights)

    override fun getFavoriteFlights(): Flow<List<FavoriteFlight>> {
        return flowOf(testFavoriteFlights)
    }

    override suspend fun addFlightToFavorites(flight: FavoriteFlight) {
        if (testFavoriteFlights.all {
                it.id != flight.id
            })
            testFavoriteFlights.add(flight)

    }

    override suspend fun removeFlightFromFavorites(flight: FavoriteFlight) {
        testSomeFavoriteFlights.remove(flight)
    }
}
