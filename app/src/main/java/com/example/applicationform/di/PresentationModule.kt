package com.example.applicationform.di

import com.example.applicationform.presentation.application.view_model.ApplicationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<ApplicationViewModel> {
        ApplicationViewModel(
            getAllHousesUseCase = get(),
            getAllStreetsUseCase = get()
        )
    }
}