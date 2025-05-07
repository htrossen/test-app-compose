package com.example.testappcompose.core.net

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FlipperModule {
    @Provides
    @Singleton
    fun provideFlipperNetwork() = NetworkFlipperPlugin()

    @Provides
    @Singleton
    @IntoSet
    fun provideFlipperInterceptor(
        @Named("flipperInterceptor") flipperInterceptor: Interceptor,
    ): Interceptor = flipperInterceptor

    @Provides
    @Singleton
    @Named("flipperInterceptor")
    fun provideNamedFlipperInterceptor(networkPlugin: NetworkFlipperPlugin): Interceptor =
        FlipperOkhttpInterceptor(networkPlugin, true)

    @Provides
    @Singleton
    fun provideFlipperInitializer(networkFlipperPlugin: NetworkFlipperPlugin) =
        FlipperInitializer(networkFlipperPlugin)
}
