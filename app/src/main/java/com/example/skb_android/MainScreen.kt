package com.example.skb_android

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.NavBarItems
import com.example.skb_android.navigation.NavFavoriteRoute
import com.example.skb_android.navigation.NavHotRoute
import com.example.skb_android.navigation.NavSearchRoute
import com.example.skb_android.navigation.VacancyFullRoute
import com.example.skb_android.vacancy.presentation.screen.VacanciesFavoriteScreen
import org.koin.compose.koinInject
import com.example.skb_android.vacancy.presentation.screen.VacanciesTrendingScreen
import com.example.skb_android.vacancy.presentation.screen.VacancyFullScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val myBackStack = koinInject<MyBackStack>()

    Scaffold(
        topBar = {
            when (myBackStack.backStack.last()) {
                is VacancyFullRoute -> TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(
                            onClick = { myBackStack.removeLast() }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.material_icon_arrow_back_ios),
                                contentDescription = "Назад"
                            )
                        }
                    }
                )

                is NavHotRoute -> TopAppBar(
                    title = { Text("Горячее") }
                )

                is NavSearchRoute -> TopAppBar(
                    title = { Text("Поиск") }
                )

                is NavFavoriteRoute -> TopAppBar(
                    title = { Text("Избранное") }
                )

                else -> {}
            }
        },
        bottomBar = {
            NavigationBar {
                NavBarItems.items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(ImageVector.vectorResource(item.resourceId), null) },
                        label = { Text(item.title) },
                        selected = myBackStack.topLevelKey == item.route,
                        onClick = { myBackStack.addTopLevel(item.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = myBackStack.backStack,
            onBack = { myBackStack.removeLast() },
            modifier = Modifier.padding(innerPadding),
            entryProvider = entryProvider {
                entry<NavHotRoute> {
                    VacanciesTrendingScreen()
                }
                entry<NavSearchRoute> {
                    Text("Search routes")
                }
                entry<NavFavoriteRoute> {
                    VacanciesFavoriteScreen()
                }
                entry<VacancyFullRoute> {
                    VacancyFullScreen(it.vacancyId)
                }
            }
        )
    }
}
