package com.example.testappcompose.core.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Favorite::class,
    ],
    exportSchema = false,
    version = 1,
)

internal abstract class OneDb : RoomDatabase() {
    abstract fun personalizationDao(): PersonalizationDao
}
