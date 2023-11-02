package com.camihruiz24.flight_search

import com.camihruiz24.flight_search.data.repository.AirportsRepository
import com.camihruiz24.flight_search.data.repository.AirportsRepositoryModule
import com.camihruiz24.flight_search.data.repository.FlightsRepository
import com.camihruiz24.flight_search.data.repository.FlightsRepositoryModule
import com.camihruiz24.flight_search.data.repository.fake.FakeAirportsRepository
import com.camihruiz24.flight_search.data.repository.fake.FakeFlightsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AirportsRepositoryModule::class]
)
abstract class FakeAirportsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAirportRepository(
        fakeRepositoryImpl: FakeAirportsRepository
    ): AirportsRepository

}

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FlightsRepositoryModule::class]
)
abstract class FakeFlightsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAirportRepository(
        fakeRepositoryImpl: FakeFlightsRepository
    ): FlightsRepository

}

