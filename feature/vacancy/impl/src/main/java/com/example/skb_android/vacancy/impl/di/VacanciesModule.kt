package com.example.skb_android.vacancy.impl.di

import com.example.skb_android.vacancy.api.domain.interactor.VacancyInteractor
import org.koin.core.module.dsl.viewModel
import com.example.skb_android.vacancy.impl.data.api.VacanciesApi
import com.example.skb_android.vacancy.impl.data.mapper.VacanciesFilterMapper
import com.example.skb_android.vacancy.impl.data.mapper.VacanciesMapper
import com.example.skb_android.vacancy.impl.data.repository.VacanciesFavoritesRepository
import com.example.skb_android.vacancy.impl.data.repository.VacanciesRepository
import com.example.skb_android.vacancy.impl.domain.interactor.VacancyInteractorImpl
import com.example.skb_android.vacancy.impl.presentation.cache.FilterBadgeCache
import com.example.skb_android.vacancy.impl.presentation.mapper.VacanciesPresentationMapper
import com.example.skb_android.vacancy.impl.presentation.viewModel.VacanciesFavoriteViewModel
import com.example.skb_android.vacancy.impl.presentation.viewModel.VacanciesTrendingViewModel
import com.example.skb_android.vacancy.impl.presentation.viewModel.VacancyFullScreenViewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val vacanciesModule = module {
    single { get<Retrofit>().create(VacanciesApi::class.java) }

    single { VacanciesMapper() }
    single { VacanciesFilterMapper() }
    single { VacanciesPresentationMapper() }
    single { FilterBadgeCache() }
    single { VacanciesRepository(get(), get(), get(), get()) }
    single { VacanciesFavoritesRepository(get(), get()) }
    single<VacancyInteractor> { VacancyInteractorImpl(get(), get()) }

    viewModel { VacanciesTrendingViewModel(get(), get(), get(), get()) }
    viewModel { VacancyFullScreenViewModel(get(), get(), get()) }
    viewModel { VacanciesFavoriteViewModel(get(), get()) }
}