package com.camihruiz24.flight_search.data.repository

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.NamePassengersTuple
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

/**
 * Repository that provides the retrieval of [Airport] from a given data source.
 */
interface AirportsRepository {
    /**
     * Retrieve all the [Airport]s from the airport table given a [search] string.
     * If search = "", retrieves all the possible airports.
     * If search does not coincide with neither iata_code or name, an empty list is returned.
     */
    fun getAirportsResultsBySearch(search: String): Flow<List<Airport>>

    /**
     * Retrieve the name and the passengers of an [Airport] from the datasource given its [iataCode]
     */
    fun getAirportNameAndPassengersByIataCode(iataCode: String): Flow<NamePassengersTuple>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AirportsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAirportRepository(
        repositoryImpl: OfflineAirportsRepository
    ): AirportsRepository

}

