package com.example.skb_android.vacancy.di

import org.koin.core.module.dsl.viewModel
import com.example.skb_android.vacancy.data.api.VacanciesApi
import com.example.skb_android.vacancy.data.mapper.VacanciesFilterMapper
import com.example.skb_android.vacancy.data.mapper.VacanciesMapper
import com.example.skb_android.vacancy.data.repository.VacanciesFavoritesRepository
import com.example.skb_android.vacancy.data.repository.VacanciesRepository
import com.example.skb_android.vacancy.domain.interactor.VacancyInteractor
import com.example.skb_android.vacancy.presentation.mapper.VacanciesPresentationMapper
import com.example.skb_android.vacancy.presentation.viewModel.VacanciesFavoriteViewModel
import com.example.skb_android.vacancy.presentation.viewModel.VacanciesTrendingViewModel
import com.example.skb_android.vacancy.presentation.viewModel.VacancyFullScreenViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val vacanciesModule = module {
    single { get<Retrofit>().create(VacanciesApi::class.java) }

    single { VacanciesMapper() }
    single { VacanciesFilterMapper() }
    single { VacanciesPresentationMapper() }
    single { VacanciesRepository(get(), get(), get(), get()) }
    single { VacanciesFavoritesRepository(get(), get()) }
    single { VacancyInteractor(get(), get()) }

    viewModel { VacanciesTrendingViewModel(get(), get(), get()) }
    viewModel { VacancyFullScreenViewModel(get(), get()) }
    viewModel { VacanciesFavoriteViewModel(get(), get()) }
}