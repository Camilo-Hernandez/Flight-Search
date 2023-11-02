package com.camihruiz24.flight_search.data.repository

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.database.FavoriteFlightDao
import com.camihruiz24.flight_search.data.database.toCompleteFlightsList
import com.camihruiz24.flight_search.data.model.Flight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineFlightsRepository @Inject constructor(
    private val favoriteFlightDao: FavoriteFlightDao,
) : FlightsRepository {

    override fun getAllPossibleFlightsFromAirports(
        airportsFlow: Flow<List<Airport>>,
        completeAirportList: List<Airport>,
    ): Flow<List<Flight>> =
        airportsFlow.map {
            it.toCompleteFlightsList(completeAirportList)
        }

    override fun getFavoriteFlights(): Flow<List<FavoriteFlight>> =
        favoriteFlightDao.getFavoriteFlights()

    override suspend fun addFlightToFavorites(flight: FavoriteFlight) =
        favoriteFlightDao.addFlightToFavorites(flight)

    override suspend fun removeFlightFromFavorites(flight: FavoriteFlight) =
        favoriteFlightDao.removeFlightFromFavorites(flight)

}

