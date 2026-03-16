package com.example.skb_android.navigation

interface Route

data object NavHotRoute : Route
data object NavSearchRoute : Route

data class VacancyFullRoute(val vacancyId: String) : Route
