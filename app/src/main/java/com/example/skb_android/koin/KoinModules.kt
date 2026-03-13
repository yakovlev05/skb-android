package com.example.skb_android.koin

import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.NavHotRoute
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import vacancy.repository.VacanciesFavoritesRepository
import vacancy.viewModel.VacanciesTrendingViewModel
import vacancy.viewModel.VacancyFullScreenViewModel

val koinModule = module {
    single { MyBackStack(NavHotRoute) }
    single { VacanciesFavoritesRepository() }

    viewModel { VacanciesTrendingViewModel(get()) }
    viewModel { VacancyFullScreenViewModel(get()) }
}
