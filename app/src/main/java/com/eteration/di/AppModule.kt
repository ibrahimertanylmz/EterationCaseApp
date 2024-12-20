package com.eteration.di

import com.eteration.core.dispatchers.AppDispatcher
import com.eteration.core.dispatchers.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideAppDispatcher(): Dispatcher {
        return AppDispatcher()
    }
}