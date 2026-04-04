package com.example.skb_android.vacancy.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.VacancyFullRoute
import com.example.skb_android.util.launchCatching
import com.example.skb_android.vacancy.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.presentation.mapper.VacanciesPresentationMapper
import com.example.skb_android.vacancy.presentation.model.ShortVacancyUiModel
import com.example.skb_android.vacancy.presentation.model.VacanciesFavoriteState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VacanciesFavoriteViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val vacanciesPresentationMapper: VacanciesPresentationMapper
) : ViewModel() {

    private val _mutableVacanciesState = MutableStateFlow(VacanciesFavoriteState())

    val vacanciesState = _mutableVacanciesState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        updateVacanciesState(VacanciesFavoriteState.State.Loading)
        launchCatching(
            onError = {
                Log.e(TAG, "Failed to load favorites vacancies: ${it.message}")
            }
        ) {
            updateVacanciesState(VacanciesFavoriteState.State.Loading)
            vacancyInteractor.observeAllFavorites()
                .collect { vacancies ->
                    updateVacanciesState(
                        VacanciesFavoriteState.State.Success(
                            vacanciesPresentationMapper.mapToShortUi(vacancies)
                        )
                    )
                }
        }
    }

    fun onVacancyClick(vacancy: ShortVacancyUiModel) {}

    fun onFavoriteClick(vacancy: ShortVacancyUiModel) {
        launchCatching(
            onError = { Log.e(TAG, "Failed to delete favorite: ${it.message}") }
        ) {
            vacancyInteractor.deleteFavorite(vacancy.id)
        }
    }

    private fun updateVacanciesState(state: VacanciesFavoriteState.State) {
        _mutableVacanciesState.update { it.copy(state = state) }
    }

    companion object {
        private val TAG = VacanciesFavoriteViewModel::class.simpleName
    }
}