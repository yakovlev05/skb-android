package com.example.skb_android.vacancy.impl.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.vacancy.api.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.api.domain.model.VacancyEntity
import com.example.skb_android.vacancy.impl.presentation.model.VacancyFullState
import com.example.skb_android.vacancy.impl.presentation.model.VacancyFullUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VacancyFullScreenViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val myBackStack: MyBackStack,
    private val vacancyId: String
) : ViewModel() {

    private val _mutableState = MutableStateFlow(VacancyFullState())

    val viewState = _mutableState.asStateFlow()

    init {
        loadVacancy()
    }

    fun onClickBack() {
        myBackStack.removeLast()
    }

    private fun loadVacancy() {
        viewModelScope.launch {
            updateState(VacancyFullState.State.Loading)
            runCatching { vacancyInteractor.getVacancy(vacancyId) }
                .onSuccess { vacancy ->
                    updateState(VacancyFullState.State.Success(mapToUi(vacancy)))
                }
                .onFailure {
                    Log.e(TAG, "Failed to load vacancy: ${it.message}")
                    updateState(VacancyFullState.State.Error(it.message.orEmpty()))
                }
        }
    }

    private fun updateState(state: VacancyFullState.State) {
        _mutableState.update { it.copy(state = state) }
    }

    private fun mapToUi(vacancy: VacancyEntity): VacancyFullUiModel = VacancyFullUiModel(
        id = vacancy.id,
        vacancyUrl = vacancy.vacancyUrl,
        name = vacancy.name,
        prettySalary = toPrettySalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryModeName),
        publishedAt = vacancy.publishedAt,
        employerName = vacancy.employerName,
        employerUrl = vacancy.employerUrl,
        employerLogoUrl = vacancy.employerLogoUrl,
        areaName = vacancy.areaName,
        experienceName = vacancy.experienceName,
        description = vacancy.description,
        skills = vacancy.skills,
        isFavorite = vacancy.isFavorite
    )

    companion object {
        private val TAG = VacancyFullScreenViewModel::class.simpleName
    }
}
