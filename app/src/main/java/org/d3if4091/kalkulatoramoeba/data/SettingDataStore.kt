package org.d3if4091.kalkulatoramoeba.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val DATABASE_PREFERENCES_NAME = "database_preferences"
val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = DATABASE_PREFERENCES_NAME
)
class SettingDataStore(preference_datastore: DataStore<Preferences>) {
    private val IS_SAVE_DAGABASE_MANAGER = booleanPreferencesKey("is_save_database_manager")

    val preferenceFlow = preference_datastore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences -> preferences[IS_SAVE_DAGABASE_MANAGER] ?: true }

    suspend fun saveSettingToPreferencesStore(isSaveDatabaseManager: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_SAVE_DAGABASE_MANAGER] = isSaveDatabaseManager
        }
    }
}
