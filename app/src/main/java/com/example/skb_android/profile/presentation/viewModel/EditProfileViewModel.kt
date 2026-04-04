package com.example.skb_android.profile.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.profile.domain.interactor.ProfileInteractor
import com.example.skb_android.profile.domain.model.ProfileEntity
import com.example.skb_android.profile.presentation.model.EditProfileState
import com.example.skb_android.util.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.core.net.toUri

class EditProfileViewModel(
    private val profileInteractor: ProfileInteractor,
    private val myBackStack: MyBackStack,
) : ViewModel() {
    private val _mutableEditProfileState = MutableStateFlow(EditProfileState())

    val editProfileState = _mutableEditProfileState.asStateFlow()

    init {
        launchCatching {
            profileInteractor.getProfileInfo().collect { profile ->
                _mutableEditProfileState.update { current ->
                    current.copy(
                        fullName = profile.fullName ?: "",
                        avatarURI = profile.avatarURI,
                        resumeURL = profile.resumeURL ?: ""
                    )
                }
            }
        }
    }

    fun onBackClick() {
        myBackStack.removeLast()
    }

    fun onSaveClick() {
        myBackStack.removeLast()
        launchCatching {
            val state = _mutableEditProfileState.value

            val avatarPath = state.avatarURI?.let { profileInteractor.saveAvatar(it.toUri()) }
            profileInteractor.saveProfileInfo(mapToEntity(state.copy(avatarURI = avatarPath)))
        }
    }

    fun onInputFullName(fullName: String) {
        _mutableEditProfileState.update { current ->
            current.copy(fullName = fullName)
        }
    }

    fun onInputResumeUrl(resumeURL: String) {
        _mutableEditProfileState.update { current ->
            current.copy(resumeURL = resumeURL)
        }
    }

    fun onCloseAlertChooseSource() {
        _mutableEditProfileState.update { current ->
            current.copy(isShowAlertChooseSource = false)
        }
    }

    fun onImageSelected(uri: Uri?) {
        uri ?: return
        _mutableEditProfileState.update { current ->
            current.copy(avatarURI = uri.toString())
        }
    }

    fun onClickAvatar() {
        _mutableEditProfileState.update { current ->
            current.copy(isShowAlertChooseSource = true)
        }
    }

    private fun mapToEntity(editState: EditProfileState): ProfileEntity = ProfileEntity(
        fullName = editState.fullName.ifEmpty { null },
        resumeURL = editState.resumeURL.ifEmpty { null },
        avatarURI = editState.avatarURI?.ifEmpty { null }
    )

}