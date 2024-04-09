package com.dtusystem.nettychatclient.di

import com.dtusystem.nettychatclient.network.ClientBinder
import com.dtusystem.nettychatclient.service.ExampleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ServiceModule {

    @Provides
    fun provideExampleService(clientBinder: ClientBinder): ExampleService {
        return clientBinder.create(ExampleService::class.java)
    }

}