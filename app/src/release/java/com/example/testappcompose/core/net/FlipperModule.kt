package com.example.testappcompose.core.net

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FlipperModule {

    @Provides
    @Singleton
    fun provideFlipperInitializer() = FlipperInitializer()
}
