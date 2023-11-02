package com.camihruiz24.flight_search.data.repository

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.FavoriteFlight
import com.camihruiz24.flight_search.data.model.Flight
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

/**
 * Repository that provides insert, delete, and retrieve [Flight] and [FavoriteFlight] through the Dao.
 */
interface FlightsRepository {

    /**
     * Creates a flow of list of [Flight]s given a list of [Airport]s flow and the complete list of airports from the DB
     */
    fun getAllPossibleFlightsFromAirports(
        airportsFlow: Flow<List<Airport>>,
        completeAirportList: List<Airport>,
    ): Flow<List<Flight>>

    /**
     * Retrieve all the [FavoriteFlight]s from the favorite flights table.
     */
    fun getFavoriteFlights(): Flow<List<FavoriteFlight>>

    /**
     * Insert [FavoriteFlight] in the favorite table
     */
    suspend fun addFlightToFavorites(flight: FavoriteFlight)

    /**
     * Remove a [FavoriteFlight] from the favorites table
     */
    suspend fun removeFlightFromFavorites(flight: FavoriteFlight)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class FlightsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteFlightRepository(
        repositoryImpl: OfflineFlightsRepository
    ): FlightsRepository

}

