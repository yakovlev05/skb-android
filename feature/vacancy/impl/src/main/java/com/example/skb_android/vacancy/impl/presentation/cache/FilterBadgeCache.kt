package com.example.skb_android.vacancy.impl.presentation.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterBadgeCache {

    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters = _hasActiveFilters.asStateFlow()

    fun update(isDefault: Boolean) {
        _hasActiveFilters.value = !isDefault
    }
}
