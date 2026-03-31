package com.example.skb_android.vacancy.presentation.model

data class VacanciesSearchQueryState(
    val text: String = "",
    val experience: VacancyExperience? = null
)

enum class VacancyExperience(val title: String) {
    NO_EXPERIENCE("без опыта"),
    BETWEEN_1_AND_3("от 1 года до 3 лет"),
    BETWEEN_3_AND_6("от 3 до 6 лет"),
    MORE_THAN_6("более 6 лет")
}
