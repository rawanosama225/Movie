package com.example.myfinalproject.Model.Repo

import com.example.myfinalproject.Model.DB.FavoriteMovieDao
import com.example.myfinalproject.Model.Data.FavoriteMovie
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val dao: FavoriteMovieDao)
 {

    suspend fun getAllFavorites(userId:String): List<FavoriteMovie> {
        return dao.getAllFavorites(userId = userId)
    }


    fun getAllFavoritesFlow(): Flow<List<FavoriteMovie>> {
        return dao.getAllFavoritesFlow()
    }


    fun getFavoriteIdsFlow(userId: String): Flow<List<Int>> {
        return dao.getFavoriteIdsFlow(userId = userId)
    }

    suspend fun addFavorite(movie: FavoriteMovie) {
        dao.insertFavorite(movie)
    }

    suspend fun removeFavorite(movieId: Int, userId: String) {
        dao.deleteFavorite(movieId,userId)
    }

    suspend fun isFavorite(movieId: Int, userId: String): Boolean {
        return dao.isFavorite(movieId,userId)
    }
}
