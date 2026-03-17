package com.example.skb_android.vacancy.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
class FullVacancyResponse(
    val id: String,
    @SerialName("alternate_url")
    val alternateUrl: String,
    val name: String,
    @SerialName("salary_range")
    val salaryRange: SalaryRangeResponse?,
    @SerialName("published_at")
    val publishedAt: String,
    val employer: EmployerInfoResponse,
    val area: AreaInfoResponse,
    val experience: VacancyExperienceResponse?,
    val description: String,
    @SerialName("key_skills")
    val skills: List<VacancySkillResponse>
)
