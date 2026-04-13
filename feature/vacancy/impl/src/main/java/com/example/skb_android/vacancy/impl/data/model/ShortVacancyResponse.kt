package com.example.skb_android.vacancy.impl.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Keep
@Serializable
class ShortVacancyResponse(
    val id: String,
    @SerialName("alternate_url")
    val alternateUrl: String,
    val name: String,
    @SerialName("salary_range")
    val salaryRange: SalaryRangeResponse?,
    @SerialName("published_at")
    val publishedAt: String,
    val employer: EmployerInfoResponse,
    val area: AreaInfoResponse
)