package com.example.testappcompose.core.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class PersonalizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveFavorite(favorite: Favorite)

    @Query("DELETE FROM Favorite WHERE id = :id")
    abstract suspend fun deleteFavoriteById(id: String)

    @Transaction
    @Query("SELECT * FROM Favorite")
    abstract fun getFavorites(): Flow<List<Favorite>>

    @Transaction
    @Query("SELECT * FROM Favorite WHERE id = :id")
    abstract suspend fun isFavorite(id: String): Favorite?
}
