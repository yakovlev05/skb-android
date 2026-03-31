package com.example.skb_android.vacancy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.VacancyFullRoute
import com.example.skb_android.util.launchCatching
import com.example.skb_android.vacancy.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.domain.model.Experience
import com.example.skb_android.vacancy.domain.model.VacancyEntity
import com.example.skb_android.vacancy.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.presentation.model.VacanciesSearchQueryState
import com.example.skb_android.vacancy.presentation.model.VacanciesTrendingState
import com.example.skb_android.vacancy.presentation.model.VacancyExperience
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VacanciesTrendingViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val myBackStack: MyBackStack
) : ViewModel() {

    private val _mutableVacanciesState = MutableStateFlow(VacanciesTrendingState())
    private val _mutableSearchState = MutableStateFlow(VacanciesSearchQueryState())

    val vacanciesState = _mutableVacanciesState.asStateFlow()
    val searchState = _mutableSearchState.asStateFlow()

    init {
        initFilterFlow()
    }

    fun onFavoriteClick(vacancy: ShortVacancyUiModel) {
        vacancyInteractor.toggleFavoriteVacancy(vacancy.id)

        _mutableVacanciesState.update { current ->
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

    fun onSearchQueryInput(text: String) {
        _mutableSearchState.update { it.copy(text = text) }
    }

    fun onSelectExperience(vacancyExperience: VacancyExperience) {
        var newExperience: VacancyExperience? = null
        _mutableSearchState.update {
            newExperience = if (it.experience == vacancyExperience) null else vacancyExperience
            it.copy(experience = newExperience)
        }

        launchCatching {
            vacancyInteractor.setExperienceFilter(mapToEntity(newExperience))
        }

    }

    fun onSearchClick() {
        viewModelScope.launch {
            vacancyInteractor.setSearchQuery(searchState.value.text)
        }
    }

    private fun initFilterFlow() {
        launchCatching {
            combine(
                vacancyInteractor.observeSearchQuery(),
                vacancyInteractor.observeExperienceFilter()
            ) { text, experience ->
                _mutableSearchState.update {
                    it.copy(text = text, experience = mapToUi(experience))
                }
            }
                .collect { loadVacancies() }
        }
    }

    private fun loadVacancies() {
        launchCatching(
            onError = {
                Log.e(TAG, "Failed to load vacancies: ${it.message}")
                updateVacanciesState(VacanciesTrendingState.State.Error(it.message.orEmpty()))
            }
        ) {
            updateVacanciesState(VacanciesTrendingState.State.Loading)
            val vacancies = vacancyInteractor.getVacancies(
                textSearch = _mutableSearchState.value.text,
                experience = mapToEntity(_mutableSearchState.value.experience)
            )
            updateVacanciesState(VacanciesTrendingState.State.Success(mapToUi(vacancies)))
        }
    }

    private fun updateVacanciesState(state: VacanciesTrendingState.State) {
        _mutableVacanciesState.update { it.copy(state = state) }
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

    private fun mapToEntity(experience: VacancyExperience?): Experience? =
        when (experience) {
            VacancyExperience.NO_EXPERIENCE -> Experience.NO_EXPERIENCE
            VacancyExperience.BETWEEN_1_AND_3 -> Experience.BETWEEN_1_AND_3
            VacancyExperience.BETWEEN_3_AND_6 -> Experience.BETWEEN_3_AND_6
            VacancyExperience.MORE_THAN_6 -> Experience.MORE_THAN_6
            null -> null
        }

    private fun mapToUi(experience: Experience?): VacancyExperience? =
        when (experience) {
            Experience.NO_EXPERIENCE -> VacancyExperience.NO_EXPERIENCE
            Experience.BETWEEN_1_AND_3 -> VacancyExperience.BETWEEN_1_AND_3
            Experience.BETWEEN_3_AND_6 -> VacancyExperience.BETWEEN_3_AND_6
            Experience.MORE_THAN_6 -> VacancyExperience.MORE_THAN_6
            null -> null
        }

    companion object {
        private val TAG = VacanciesTrendingViewModel::class.simpleName
    }
}
