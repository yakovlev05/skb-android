package com.example.skb_android.koin

import com.example.skb_android.navigation.MyBackStack
import com.example.skb_android.navigation.NavHotRoute
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.example.skb_android.vacancy.repository.VacanciesFavoritesRepository
import com.example.skb_android.vacancy.viewModel.VacanciesTrendingViewModel
import com.example.skb_android.vacancy.viewModel.VacancyFullScreenViewModel

val koinModule = module {
    single { MyBackStack(NavHotRoute) }
    single { VacanciesFavoritesRepository() }

    viewModel { VacanciesTrendingViewModel(get()) }
    viewModel { VacancyFullScreenViewModel(get()) }
}
