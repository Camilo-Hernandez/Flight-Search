package com.camihruiz24.flight_search.data.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppDatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideAirportDao(appDatabase: AppDatabase): AirportDao {
        return appDatabase.getAirportDao()
    }

    @Provides
    fun provideFavoriteFlightDao(appDatabase: AppDatabase): FavoriteFlightDao {
        return appDatabase.getFavoriteFlightDao()
    }

}