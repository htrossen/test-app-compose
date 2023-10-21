package com.example.testappcompose.core.di

import com.example.testappcompose.core.data.PersonalizationDao
import com.example.testappcompose.core.net.CocktailApi
import com.example.testappcompose.core.service.CocktailService
import com.example.testappcompose.core.service.CocktailServiceImpl
import com.example.testappcompose.repo.PersonalizationRepo
import com.example.testappcompose.repo.PersonalizationRepoImpl
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
    internal fun provideCocktailService(
        cocktailApi: CocktailApi
    ): CocktailService = CocktailServiceImpl(
        cocktailApi
    )

    @Provides
    @Singleton
    internal fun providePersonalizationRepo(
        personalizationDao: PersonalizationDao
    ): PersonalizationRepo = PersonalizationRepoImpl(
        personalizationDao
    )
}
