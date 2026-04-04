package com.example.skb_android.profile.di


import com.example.skb_android.profile.data.repository.ProfileRepository
import com.example.skb_android.profile.domain.interactor.ProfileInteractor
import com.example.skb_android.profile.presentation.viewModel.EditProfileViewModel
import com.example.skb_android.profile.presentation.viewModel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single { ProfileRepository(get()) }
    single { ProfileInteractor(get(), get()) }

    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
}