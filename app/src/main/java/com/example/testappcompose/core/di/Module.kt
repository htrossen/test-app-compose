package com.example.testappcompose.core.di

import com.example.testappcompose.core.net.TestApi
import com.example.testappcompose.core.service.TestService
import com.example.testappcompose.core.service.TestServiceImpl
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
        testApi: TestApi
    ): TestService = TestServiceImpl(
        testApi
    )
}
