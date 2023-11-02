package com.camihruiz24.flight_search.data.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    @Query("""
        SELECT * FROM airport
        WHERE iata_code = :search OR iata_code LIKE '%' || :search || '%' OR name LIKE '%' || :search || '%'
        ORDER BY passengers DESC;
    """)
    fun getAirportsBySearch(search: String): Flow<List<Airport>>

    @Query("""
        SELECT name, passengers FROM airport
        WHERE iata_code = :iataCode;
    """)
    fun getAirportNameAndPassengersByIataCode(iataCode: String): Flow<NamePassengersTuple>

    @Insert
    suspend fun insertAll(airports: List<Airport>)

}

data class NamePassengersTuple (
    @ColumnInfo("name") val name: String,
    @ColumnInfo("passengers") val passengers: Long,
)