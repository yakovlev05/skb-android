package com.example.skb_android.vacancy.impl.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
class VacancyExperienceResponse(
    val name: String?
)

@Keep
@Serializable
class VacancySkillResponse(
    val name: String
)

@Keep
@Serializable
class VacanciesResponse(
    val items: List<ShortVacancyResponse>
)

@Keep
@Serializable
class SalaryRangeModeResponse(
    val name: String?
)

@Keep
@Serializable
class SalaryRangeResponse(
    val from: Int?,
    val to: Int?,
    val mode: SalaryRangeModeResponse?
)

@Keep
@Serializable
class EmployerInfoResponse(
    val name: String,
    @SerialName("alternate_url")
    val alternateUrl: String?,
    @SerialName("logo_urls")
    val logoUrls: EmployerLogoUrlResponse?
)

@Keep
@Serializable
class EmployerLogoUrlResponse(
    @SerialName("240")
    val middleSize: String?
)

@Keep
@Serializable
class AreaInfoResponse(
    val name: String
)
