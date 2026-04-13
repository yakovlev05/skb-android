package com.example.skb_android.vacancy.impl.presentation.model

data class VacanciesTrendingState(
    val state: State = State.Loading
) {

    sealed interface State {
        object Loading : State
        data class Error(val message: String) : State
        data class Success(val vacancies: List<ShortVacancyUiModel>) : State
    }

}
