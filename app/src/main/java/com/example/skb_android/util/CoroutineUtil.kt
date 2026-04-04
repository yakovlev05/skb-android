package com.example.skb_android.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun ViewModel.launchCatching(
    onError: (Throwable) -> Unit = {},
    block: suspend () -> Unit
) {
    viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            onError(e)
        }
    }
}
