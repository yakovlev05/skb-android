package com.example.skb_android.profile.domain.interactor

import android.content.Context
import android.net.Uri
import com.example.skb_android.profile.data.repository.ProfileRepository
import com.example.skb_android.profile.domain.model.ProfileEntity
import java.io.File

class ProfileInteractor(
    private val profileRepository: ProfileRepository,
    private val context: Context,
) {
    fun getProfileInfo() = profileRepository.getProfileInfo()

    suspend fun saveProfileInfo(profileEntity: ProfileEntity) =
        profileRepository.saveProfileInfo(profileEntity)

    fun saveAvatar(sourceUri: Uri): String {
        val destFile = File(context.filesDir, AVATAR_FILE_NAME)
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            destFile.outputStream().use { output -> input.copyTo(output) }
        }
        return "${destFile.toURI()}?t=${System.currentTimeMillis()}"
    }

    companion object {
        private const val AVATAR_FILE_NAME = "avatar.jpg"
    }
}
