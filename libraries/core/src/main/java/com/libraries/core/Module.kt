package com.libraries.core

import com.libraries.core.database.PersonalizationDao
import com.libraries.core.net.CocktailApi
import com.libraries.core.repo.PersonalizationRepo
import com.libraries.core.repo.PersonalizationRepoImpl
import com.libraries.core.service.CocktailService
import com.libraries.core.service.CocktailServiceImpl
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
