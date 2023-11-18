package com.camihruiz24.flight_search.data.app_preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.camihruiz24.flight_search.MainCoroutineRule
import kotlinx.coroutines.test.TestScope

object FakeDataStore {
    val coroutineRule = MainCoroutineRule()
    private val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val testCoroutineScope = TestScope(coroutineRule.testDispatcher)

    val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile =
            { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
        )
}

private const val TEST_DATASTORE_NAME: String = "test_datastore"

