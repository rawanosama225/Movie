package com.example.myfinalproject.Model.Repo

import com.example.myfinalproject.Model.DB.FavoriteMovieDao
import com.example.myfinalproject.Model.Data.FavoriteMovie
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val dao: FavoriteMovieDao)
 {

    suspend fun getAllFavorites(): List<FavoriteMovie> {
        return dao.getAllFavorites()
    }


    fun getAllFavoritesFlow(): Flow<List<FavoriteMovie>> {
        return dao.getAllFavoritesFlow()
    }


    fun getFavoriteIdsFlow(): Flow<List<Int>> {
        return dao.getFavoriteIdsFlow()
    }

    suspend fun addFavorite(movie: FavoriteMovie) {
        dao.insertFavorite(movie)
    }

    suspend fun removeFavorite(movieId: Int) {
        dao.deleteFavorite(movieId)
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return dao.isFavorite(movieId)
    }
}
