package com.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkSingletonModule {

    @Binds
    @Singleton
    abstract fun bindNetworkApi(networkApiImpl: NetworkApiImpl): NetworkApi
}