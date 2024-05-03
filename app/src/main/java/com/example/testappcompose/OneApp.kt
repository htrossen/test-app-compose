package com.example.testappcompose

import android.app.Application
import com.example.testappcompose.core.net.FlipperInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OneApp : Application() {
    @Inject
    lateinit var flipperInitializer: FlipperInitializer

    override fun onCreate() {
        super.onCreate()

        flipperInitializer.initFlipperPlugins(this)
    }
}
