package com.example.applicationform.di

import com.example.applicationform.domain.application.GetAllHousesUseCase
import com.example.applicationform.domain.application.GetAllStreetsUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetAllStreetsUseCase> { GetAllStreetsUseCase(applicationRepository = get()) }
    factory<GetAllHousesUseCase> { GetAllHousesUseCase(applicationRepository = get()) }
}