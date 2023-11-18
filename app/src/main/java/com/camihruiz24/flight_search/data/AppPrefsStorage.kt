package com.camihruiz24.flight_search.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "app_pref_storage"
)

interface PreferencesStorage {
    // saves the last query before closing the app
     val lastSearchQuery: Flow<String>
    suspend fun setLastSearchQueryPreference(searchQuery: String)

    // check if the current layout is linear or grid
    val isDarkMode: Flow<Boolean>
    suspend fun setDarkModePreference(isDarkMode: Boolean)

    /** clears all the stored data */
    suspend fun clearPreferenceStorage()
}

@Singleton
class AppPrefsStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesStorage {

    override suspend fun clearPreferenceStorage() {
        dataStore.edit {
            it.clear()
        }
    }

    private object PreferencesKeys {
        val LAST_SEARCH_QUERY: Preferences.Key<String> = stringPreferencesKey("pref_LastSearchQuery")
        val IS_DARK_MODE: Preferences.Key<Boolean> = booleanPreferencesKey("pref_DarkMode")
    }

    override val lastSearchQuery: Flow<String>
        get() = dataStore.getValueAsFlow(PreferencesKeys.LAST_SEARCH_QUERY, "")

    override suspend fun setLastSearchQueryPreference(searchQuery: String) {
        dataStore.setValue(PreferencesKeys.LAST_SEARCH_QUERY, searchQuery)
    }

    override val isDarkMode: Flow<Boolean> =
        dataStore.getValueAsFlow(PreferencesKeys.IS_DARK_MODE, false)

    override suspend fun setDarkModePreference(isDarkMode: Boolean) {
        dataStore.setValue(PreferencesKeys.IS_DARK_MODE, isDarkMode)
    }

    /***
     * handy function to save key-value pairs in Preference. Sets or updates the value in Preference
     * @param key used to identify the preference
     * @param value the value to be saved in the preference
     */
    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

    /***
     * handy function to return Preference value based on the Preference key
     * @param key  used to identify the preference
     * @param defaultValue value in case the Preference does not exists
     * @throws Exception if there is some error in getting the value
     * @return [Flow] of [T]
     */
    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { it: Throwable ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (it is IOException) {
                // we try again to store the value in the map operator
                Log.e("AppPrefsStorage", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageBindingModule {
    @Binds
    abstract fun providesPreferenceStorage(
        appPreferenceStorage: AppPrefsStorage
    ): PreferencesStorage
}

@Module
@InstallIn(SingletonComponent::class)
object StorageProviderModule {

    @Provides
    @Singleton
    fun providePreferencesObject(@ApplicationContext context: Context): AppPrefsStorage =
        AppPrefsStorage(context.dataStore)
}