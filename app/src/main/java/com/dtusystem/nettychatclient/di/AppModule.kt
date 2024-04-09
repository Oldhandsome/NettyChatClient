package com.dtusystem.nettychatclient.di

import com.dtusystem.nettychatclient.network.ClientBinder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideClientBinder(): ClientBinder {
        return ClientBinder("192.168.0.167", 8080)
    }
}