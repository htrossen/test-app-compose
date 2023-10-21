package com.example.testappcompose.core.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String
)
