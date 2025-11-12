package com.example.myfinalproject.Model.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val voteAverage: Double
)

fun Movie.toFavoriteMovie(): FavoriteMovie {
    return FavoriteMovie(
        id = this.id,
        title = this.title ?: "",
        overview = this.overview ?: "",
        posterPath = this.poster_path ?: "",
        backdropPath = this.backdrop_path ?: "",
        releaseDate = this.release_date ?: "",
        voteAverage = this.vote_average
    )
}

fun FavoriteMovie.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        poster_path = this.posterPath,
        backdrop_path = this.backdropPath,
        release_date = this.releaseDate,
        vote_average = this.voteAverage,
        isFavorite = true
    )
}