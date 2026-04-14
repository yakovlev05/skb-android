package com.example.skb_android.profile.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.skb_android.profile.domain.interactor.ProfileInteractor
import com.example.skb_android.profile.domain.model.ProfileEntity
import com.example.skb_android.profile.presentation.model.EditProfileState
import com.example.skb_android.core.util.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.core.net.toUri
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.profile.presentation.helper.NotificationScheduler
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class EditProfileViewModel(
    private val profileInteractor: ProfileInteractor,
    private val myBackStack: MyBackStack,
    private val notificationScheduler: NotificationScheduler,
) : ViewModel() {
    private val _mutableEditProfileState = MutableStateFlow(EditProfileState())
    private val _timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val editProfileState = _mutableEditProfileState.asStateFlow()

    init {
        launchCatching {
            profileInteractor.getProfileInfo().collect { profile ->
                _mutableEditProfileState.update { current ->
                    current.copy(
                        fullName = profile.fullName ?: "",
                        avatarURI = profile.avatarURI,
                        resumeURL = profile.resumeURL ?: "",
                        time = profile.time?.let { parseTime(it) } ?: LocalTime.of(0, 0),
                        timeString = profile.time ?: ""
                    )
                }
            }
        }
    }

    fun onBackClick() {
        myBackStack.removeLast()
    }

    fun onSaveClick() {
        if (_mutableEditProfileState.value.isTimeError) {
            return
        }

        myBackStack.removeLast()
        launchCatching {
            val state = _mutableEditProfileState.value

            val avatarPath = state.avatarURI?.let { profileInteractor.saveAvatar(it.toUri()) }
            profileInteractor.saveProfileInfo(mapToEntity(state.copy(avatarURI = avatarPath)))

            if (state.time != LocalTime.of(0, 0)) {
                notificationScheduler.saveNotification(
                    state.time,
                    "Скоро начало пары, ${state.fullName.ifEmpty { "Анонимн" }}! 🎉"
                )
            }
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

    fun onInputTime(time: String) {
        if (time.isEmpty()) {
            _mutableEditProfileState.update { current ->
                current.copy(
                    timeString = "",
                    time = LocalTime.of(0, 0),
                    isTimeError = false
                )
            }
            return
        }

        try {
            val parsedTime = LocalTime.parse(time, _timeFormatter)
            _mutableEditProfileState.update { current ->
                current.copy(
                    timeString = time,
                    time = parsedTime,
                    isTimeError = false
                )
            }
        } catch (_: DateTimeParseException) {
            _mutableEditProfileState.update { current ->
                current.copy(timeString = time, isTimeError = true)
            }
        }
    }

    fun onClockClick() {
        _mutableEditProfileState.update { current ->
            current.copy(isShowTimePicker = true)
        }
    }

    fun onDismissTimePicker() {
        _mutableEditProfileState.update { current ->
            current.copy(isShowTimePicker = false)
        }
    }

    fun onConfirmTimePicker(hour: Int, minute: Int) {
        val time = LocalTime.of(hour, minute)
        _mutableEditProfileState.update { current ->
            current.copy(
                time = time,
                timeString = time.format(_timeFormatter),
                isShowTimePicker = false,
                isTimeError = false
            )
        }
    }

    private fun parseTime(time: String): LocalTime {
        return LocalTime.parse(time, _timeFormatter)
    }

    private fun mapToEntity(editState: EditProfileState): ProfileEntity = ProfileEntity(
        fullName = editState.fullName.ifEmpty { null },
        resumeURL = editState.resumeURL.ifEmpty { null },
        avatarURI = editState.avatarURI?.ifEmpty { null },
        time = if (editState.time == LocalTime.of(0, 0)) null else editState.timeString
    )

}