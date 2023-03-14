package com.example.travelassistant.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {
    private val mContext = context

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("UserSettings")
        val THEMEMODE_KEY = stringPreferencesKey("user_theme")
        val ISUSERSIGNED_KEY = booleanPreferencesKey("user_signed")
        val SELECTED_VEHICLE_KEY = stringPreferencesKey("selected_vehicle")
        val SELECTED_NAVIGATION_TYPE_KEY = stringPreferencesKey("navigation_type")
    }

    val getThemeMode: Flow<String> = mContext.dataStore.data.map { preferences ->
        preferences[THEMEMODE_KEY] ?: "System Preferences"
    }

    val getIsUserLogged: Flow<Boolean> = mContext.dataStore.data.map { preferences ->
        preferences[ISUSERSIGNED_KEY] ?: false
    }

    val getSelectedVehicle: Flow<String> = mContext.dataStore.data.map { preferences ->
        preferences[SELECTED_VEHICLE_KEY] ?: "No vehicle"
    }

    val getSelectedNavigationType: Flow<String> = mContext.dataStore.data.map { preferences ->
        preferences[SELECTED_NAVIGATION_TYPE_KEY] ?: "TravelAssistant"
    }

    suspend fun setThemeMode(theme: String) {
        mContext.dataStore.edit { preferences ->
            preferences[THEMEMODE_KEY] = theme
        }
    }

    suspend fun setIsUserLogged(isUserSigned: Boolean) {
        mContext.dataStore.edit { preferences ->
            preferences[ISUSERSIGNED_KEY] = isUserSigned
        }
    }

    suspend fun setSelectedVehicle(vehicleLicencePlate: String) {
        mContext.dataStore.edit { preferences ->
            preferences[SELECTED_VEHICLE_KEY] = vehicleLicencePlate
        }
    }

    suspend fun setSelectedNavigationType(navigationType: String) {
        mContext.dataStore.edit { preferences ->
            preferences[SELECTED_NAVIGATION_TYPE_KEY] = navigationType
        }
    }

}
