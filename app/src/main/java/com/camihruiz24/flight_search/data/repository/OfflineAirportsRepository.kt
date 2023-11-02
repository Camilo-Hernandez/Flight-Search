package com.camihruiz24.flight_search.data.repository

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.AirportDao
import com.camihruiz24.flight_search.data.database.NamePassengersTuple
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This repo gives results of the airports the user searches in the search bar
 */
@Singleton
class OfflineAirportsRepository @Inject constructor(
    private val airportDao: AirportDao
) : AirportsRepository {
    override fun getAirportsResultsBySearch(search: String): Flow<List<Airport>> =
        airportDao.getAirportsBySearch(search.trim())

    override fun getAirportNameAndPassengersByIataCode(iataCode: String): Flow<NamePassengersTuple> =
        airportDao.getAirportNameAndPassengersByIataCode(iataCode)

}
