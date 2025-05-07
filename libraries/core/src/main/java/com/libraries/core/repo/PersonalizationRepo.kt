package com.libraries.core.repo

import com.libraries.core.database.Favorite
import com.libraries.core.database.PersonalizationDao
import kotlinx.coroutines.flow.Flow

interface PersonalizationRepo {
    fun getFavorites(): Flow<List<Favorite>>
    suspend fun isFavorites(id: String): Boolean
    suspend fun saveFavorites(favorite: Favorite)
    suspend fun deleteFavorite(id: String)
}

internal class PersonalizationRepoImpl internal constructor(
    private val personalizationDao: PersonalizationDao
) : PersonalizationRepo {
    override fun getFavorites(): Flow<List<Favorite>> = personalizationDao.getFavorites()

    override suspend fun isFavorites(id: String): Boolean =
        personalizationDao.isFavorite(id) != null

    override suspend fun saveFavorites(favorite: Favorite) {
        personalizationDao.saveFavorite(favorite)
    }

    override suspend fun deleteFavorite(id: String) {
        personalizationDao.deleteFavoriteById(id)
    }
}
