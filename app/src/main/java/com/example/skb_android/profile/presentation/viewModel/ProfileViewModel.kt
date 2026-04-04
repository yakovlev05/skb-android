package com.example.skb_android.profile.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.skb_android.navigation.EditProfileRoute
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.profile.domain.interactor.ProfileInteractor
import com.example.skb_android.profile.domain.model.ProfileEntity
import com.example.skb_android.profile.presentation.model.ProfileUiModel
import com.example.skb_android.util.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    private val profileInteractor: ProfileInteractor,
    private val myBackStack: MyBackStack
) : ViewModel() {

    private val _mutableProfileState = MutableStateFlow(ProfileUiModel())

    val profileState = _mutableProfileState.asStateFlow()

    init {
        launchCatching {
            profileInteractor.getProfileInfo().collect { profile ->
                _mutableProfileState.value = mapToUi(profile)
            }
        }
    }

    fun onEditClick() {
        myBackStack.add(EditProfileRoute)
    }

    private fun mapToUi(profileEntity: ProfileEntity) = ProfileUiModel(
        fullName = profileEntity.fullName ?: "Имя не заполнено",
        avatarURI = profileEntity.avatarURI,
        resumeURL = profileEntity.resumeURL
    )
}