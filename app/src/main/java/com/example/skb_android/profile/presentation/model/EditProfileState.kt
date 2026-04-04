package com.example.skb_android.profile.presentation.model

data class EditProfileState(
    val fullName: String = "",
    val avatarURI: String? = null,
    val resumeURL: String = "",
    val isShowAlertChooseSource: Boolean = false
)