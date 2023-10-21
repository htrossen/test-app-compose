package com.example.testappcompose.core.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun provideOneDb(@ApplicationContext context: Context): OneDb = Room.databaseBuilder(
        context,
        OneDb::class.java, "one_db"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providePersonalizationDao(db: OneDb) = db.personalizationDao()
}
