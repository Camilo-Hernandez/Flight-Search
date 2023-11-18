package com.camihruiz24.flight_search.data.repository.fake

import com.camihruiz24.flight_search.data.database.Airport
import com.camihruiz24.flight_search.data.database.NamePassengersTuple
import com.camihruiz24.flight_search.data.repository.AirportsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAirportsRepository @Inject constructor() : AirportsRepository {
    override fun getAirportsResultsBySearch(search: String): Flow<List<Airport>> =
    // We can delegate the fake production to the fakeDao if we want it
        // airportDao.getAirportsBySearch(search)
        flowOf(
            FakeAirportDatasource.testAirports.filter {
                it.name.contains(search.trim(), true) ||
                        it.iataCode.contains(search.trim(), true)
            }
        )

    override fun getAirportNameAndPassengersByIataCode(iataCode: String): Flow<NamePassengersTuple> = flowOf(
        FakeAirportDatasource.testAirports
            .first { it.iataCode == iataCode }
            .let { NamePassengersTuple(it.name, it.passengers) }
    )
}
