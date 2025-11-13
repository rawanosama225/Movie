package com.example.myfinalproject.Model.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfinalproject.Model.Data.FavoriteMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies WHERE userId = :userId")
    suspend fun getAllFavorites(userId: String): List<FavoriteMovie>


    //TODO: return list of movies using flow
    @Query("SELECT * FROM favorite_movies")
    fun getAllFavoritesFlow(): Flow<List<FavoriteMovie>>


    @Query("SELECT id FROM favorite_movies")
    fun getFavoriteIdsFlow(): Flow<List<Int>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovie)

    //TODO: Delete movie where movieId and userId
    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    suspend fun deleteFavorite(movieId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}