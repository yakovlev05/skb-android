package com.example.skb_android.navigation

interface Route

data object NavHotRoute : Route
data object NavSearchRoute : Route
data object NavFavoriteRoute : Route
data object NavProfileRoute : Route
data class VacancyFullRoute(val vacancyId: String) : Route
data object EditProfileRoute : Route