package com.example.testappcompose.core.net

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import javax.inject.Inject

class FlipperInitializer @Inject constructor(
    private val networkPlugin: NetworkFlipperPlugin
) {

    fun initFlipperPlugins(context: Context?) {
        SoLoader.init(context, false)
        val client = AndroidFlipperClient.getInstance(context)

        val descriptorMapping = DescriptorMapping.withDefaults()

        client.run {
            addPlugin(InspectorFlipperPlugin(context, descriptorMapping))
            addPlugin(networkPlugin)
            addPlugin(DatabasesFlipperPlugin(context))
            addPlugin(NavigationFlipperPlugin.getInstance())
            start()
        }
    }
}
