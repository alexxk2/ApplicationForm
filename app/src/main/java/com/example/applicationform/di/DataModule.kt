package com.example.applicationform.di

import com.example.applicationform.data.converters.NetworkConverter
import com.example.applicationform.data.network.NetworkClient
import com.example.applicationform.data.network.impl.NetworkClientImpl
import com.example.applicationform.data.repository.ApplicationRepositoryImpl
import com.example.applicationform.domain.repository.ApplicationRepository
import org.koin.dsl.module

val dataModule = module {
    single<NetworkConverter> { NetworkConverter() }
    single<NetworkClient> { NetworkClientImpl() }
    single<ApplicationRepository> {ApplicationRepositoryImpl(converter = get(), networkClient = get())  }
}