package com.example.skb_android.navigation

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
            resourceId = com.example.skb_android.core.R.drawable.material_icon_search,
            route = NavSearchRoute
        ),
        NavBarItem(
            title = "Избранное",
            resourceId = com.example.skb_android.core.R.drawable.material_icon_favorite,
            route = NavFavoriteRoute
        ),
        NavBarItem(
            title = "Профиль",
            resourceId = com.example.skb_android.core.R.drawable.material_icon_profile,
            route = NavProfileRoute
        )
    )
}
