package com.example.skb_android.vacancy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.VacancyFullRoute
import com.example.skb_android.vacancy.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import com.example.skb_android.vacancy.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.presentation.model.VacanciesTrendingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VacanciesTrendingViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val myBackStack: MyBackStack
) : ViewModel() {

    private val _mutableState = MutableStateFlow(VacanciesTrendingState())

    val viewState = _mutableState.asStateFlow()

    init {
        loadVacancies()
    }

    fun onFavoriteClick(vacancy: ShortVacancyUiModel) {
        vacancyInteractor.toggleFavoriteVacancy(vacancy.id)

        _mutableState.update { current ->
            val success = current.state as? VacanciesTrendingState.State.Success ?: return
            val updatedVacancies = success.vacancies.map {
                if (it.id == vacancy.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            current.copy(state = success.copy(vacancies = updatedVacancies))
        }
    }

    fun onVacancyClick(vacancy: ShortVacancyUiModel) {
        myBackStack.add(VacancyFullRoute(vacancy.id))
    }

    private fun loadVacancies() {
        viewModelScope.launch {
            updateState(VacanciesTrendingState.State.Loading)
            runCatching { vacancyInteractor.getVacancies() }
                .onSuccess { updateState(VacanciesTrendingState.State.Success(mapToUi(it))) }
                .onFailure {
                    Log.e(TAG, "Failed to load vacancies: ${it.message}")
                    updateState(VacanciesTrendingState.State.Error(it.message.orEmpty()))
                }
        }
    }

    private fun updateState(state: VacanciesTrendingState.State) {
        _mutableState.update { it.copy(state = state) }
    }

    private fun mapToUi(vacancies: List<VacancyEntity>): List<ShortVacancyUiModel> =
        vacancies.map { vacancy ->
            ShortVacancyUiModel(
                id = vacancy.id,
                vacancyUrl = vacancy.vacancyUrl,
                name = vacancy.name,
                prettySalary = toPrettySalary(
                    vacancy.salaryFrom,
                    vacancy.salaryTo,
                    vacancy.salaryModeName
                ),
                publishedAt = vacancy.publishedAt,
                employerName = vacancy.employerName,
                employerUrl = vacancy.employerUrl,
                employerLogoUrl = vacancy.employerLogoUrl,
                areaName = vacancy.areaName,
                isFavorite = false
            )
        }

    companion object {
        private val TAG = VacanciesTrendingViewModel::class.simpleName
    }
}
