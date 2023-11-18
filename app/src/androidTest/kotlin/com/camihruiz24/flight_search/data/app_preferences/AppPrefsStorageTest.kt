package com.camihruiz24.flight_search.data.app_preferences

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.camihruiz24.flight_search.data.AppPrefsStorage
import com.camihruiz24.flight_search.data.PreferencesStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AppPrefsStorageTest {

    private val appPrefsRepository: PreferencesStorage =
        AppPrefsStorage(FakeDataStore.testDataStore)

    @Test
    fun repository_testFetchInitialPreferences() = runTest(FakeDataStore.coroutineRule.testDispatcher) {
        val expectedStringQuery: String = ""
        val isDarkModePreference = false
        assertEquals(expectedStringQuery, appPrefsRepository.lastSearchQuery.first())
        assertEquals(isDarkModePreference, appPrefsRepository.isDarkMode.first())
    }

    @Test
    fun repository_testWriteSearchQuery() = runTest(FakeDataStore.coroutineRule.testDispatcher) {
        val expectedStringQuery = "hum"
        appPrefsRepository.setLastSearchQueryPreference(expectedStringQuery)
        assertEquals(expectedStringQuery, appPrefsRepository.lastSearchQuery.first())
    }

    @Test
    fun repository_testChangeDarkModePreference() = runTest(FakeDataStore.coroutineRule.testDispatcher) {
        val isDarkModePreference = true
        appPrefsRepository.setDarkModePreference(isDarkModePreference)
        assertEquals(isDarkModePreference, appPrefsRepository.isDarkMode.first())
    }

    @Test
    fun repository_testClearPreferences() = runTest(FakeDataStore.coroutineRule.testDispatcher) {
        val expectedStringQuery = "hum"
        val isDarkModePreference = true
        appPrefsRepository.setDarkModePreference(isDarkModePreference)
        appPrefsRepository.setLastSearchQueryPreference(expectedStringQuery)
        appPrefsRepository.clearPreferenceStorage()
        assertEquals("", appPrefsRepository.lastSearchQuery.first())
        assertEquals(false, appPrefsRepository.isDarkMode.first())
    }
}
