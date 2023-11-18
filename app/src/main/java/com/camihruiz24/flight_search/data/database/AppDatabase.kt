package com.camihruiz24.flight_search.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Airport::class, FavoriteFlight::class],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getAirportDao(): AirportDao
    abstract fun getFavoriteFlightDao(): FavoriteFlightDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            /**
             * Multiple threads can potentially ask for a database instance at the same time,
             * which results in two databases instead of one. This issue is known as a race
             * condition. Wrapping the code to get the database inside a synchronized block means
             * that only one thread of execution at a time can enter this block of code, which
             * makes sure the database only gets initialized once.
             */
            Instance ?: synchronized(this) { // This is the companion object
                Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = "flight_database"
                )
                    .createFromAsset("database/flight_search.db")
                    // Wipes and rebuilds instead of migrating if no Migration object.
//                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        Instance = it
                    }
            }
    }
}