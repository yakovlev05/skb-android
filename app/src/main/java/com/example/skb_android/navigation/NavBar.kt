package com.example.skb_android.navigation

import com.example.skb_android.R

data class NavBarItem(
    val title: String,
    val resourceId: Int,
    val route: Route
)

object NavBarItems {
    val items = listOf(
        NavBarItem(
            title = "Горячее",
            resourceId = R.drawable.material_icon_whatshot,
            route = NavHotRoute
        ),
        NavBarItem(
            title = "Поиск",
            resourceId = R.drawable.material_icon_search,
            route = NavSearchRoute
        )
    )
}
