package com.example.skb_android.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

class MyBackStack(startRoute: Route) {

    private val topLevelStacks: LinkedHashMap<Route, SnapshotStateList<Route>> = linkedMapOf(
        startRoute to mutableStateListOf(startRoute)
    )

    var topLevelKey by mutableStateOf(startRoute)
        private set

    val backStack = mutableStateListOf(startRoute)

    private fun updateBackStack() = backStack.apply {
        clear()
        addAll(topLevelStacks.flatMap { it.value })
    }

    fun addTopLevel(key: Route) {
        if (topLevelStacks[key] == null) {
            topLevelStacks[key] = mutableStateListOf(key)
        } else {
            topLevelStacks.remove(key)?.let { topLevelStacks[key] = it }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: Route) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val currentStack = topLevelStacks[topLevelKey] ?: return
        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else {
            topLevelStacks.remove(topLevelKey)
            topLevelKey = topLevelStacks.keys.last()
        }
        updateBackStack()
    }

}
