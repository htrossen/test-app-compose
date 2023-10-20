package com.example.testappcompose.core.di

import com.example.testappcompose.core.net.CocktailApi
import com.example.testappcompose.core.service.CocktailService
import com.example.testappcompose.core.service.CocktailServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    internal fun provideTestService(
        cocktailApi: CocktailApi
    ): CocktailService = CocktailServiceImpl(
        cocktailApi
    )
}
