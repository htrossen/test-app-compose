package com.libraries.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String
)
