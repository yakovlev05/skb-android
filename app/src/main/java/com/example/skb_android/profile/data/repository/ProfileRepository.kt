package com.example.skb_android.profile.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.MutablePreferences
import com.example.skb_android.profile.domain.model.ProfileEntity
import com.example.skb_android.util.setOrRemove
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(
    private val dataStore: DataStore<Preferences>
) {

    fun getProfileInfo(): Flow<ProfileEntity> = dataStore.data.map {
        ProfileEntity(
            fullName = it[profileFullName],
            avatarURI = it[avatarURI],
            resumeURL = it[resumeURL],
            time = it[time]
        )
    }

    suspend fun saveProfileInfo(profileEntity: ProfileEntity) {
        dataStore.edit { preferences ->
            preferences.setOrRemove(profileFullName, profileEntity.fullName)
            preferences.setOrRemove(avatarURI, profileEntity.avatarURI)
            preferences.setOrRemove(resumeURL, profileEntity.resumeURL)
            preferences.setOrRemove(time, profileEntity.time)
        }
    }

    companion object {
        val profileFullName = stringPreferencesKey("PROFILE_FULL_NAME")
        val avatarURI = stringPreferencesKey("PROFILE_AVATAR_URI")
        val resumeURL = stringPreferencesKey("PROFILE_RESUME_URL")
        val time = stringPreferencesKey("PROFILE_TIME")
    }
}