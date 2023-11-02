package com.camihruiz24.flight_search.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteFlightDao {
    @Query("SELECT * FROM favorite")
    fun getFavoriteFlights(): Flow<List<FavoriteFlight>>

    @Insert
    suspend fun addFlightToFavorites(flight: FavoriteFlight)

    @Delete
    suspend fun removeFlightFromFavorites(flight: FavoriteFlight)
}
