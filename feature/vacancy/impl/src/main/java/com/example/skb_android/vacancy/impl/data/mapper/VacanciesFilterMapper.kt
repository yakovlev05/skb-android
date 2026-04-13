package com.example.skb_android.vacancy.impl.data.mapper

import com.example.skb_android.vacancy.api.domain.model.Experience

class VacanciesFilterMapper {
    fun mapToApi(experience: Experience?): String? {
        Experience.NO_EXPERIENCE
        return when (experience) {
            Experience.NO_EXPERIENCE -> "noExperience"
            Experience.BETWEEN_1_AND_3 -> "between1And3"
            Experience.BETWEEN_3_AND_6 -> "between3And6"
            Experience.MORE_THAN_6 -> "moreThan6"
            else -> null
        }
    }
}