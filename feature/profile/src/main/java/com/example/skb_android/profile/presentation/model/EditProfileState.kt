package com.example.skb_android.profile.presentation.model

import java.time.LocalTime

data class EditProfileState(
    val fullName: String = "",
    val avatarURI: String? = null,
    val resumeURL: String = "",
    val isShowAlertChooseSource: Boolean = false,
    val time: LocalTime = LocalTime.of(0, 0),
    val timeString: String = "",
    val isTimeError: Boolean = false,
    val isShowTimePicker: Boolean = false,
)