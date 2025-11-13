package com.example.myfinalproject.Model.Data

data class Movie(

    val id: Int,
    val userId: String?,
    val title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val release_date: String?,
    val vote_average: Double = 0.0,
    val isFavorite: Boolean = false,
) {

    val posterUrl: String
        get() = "https://image.tmdb.org/t/p/w500${poster_path}"

    val backdropUrl: String
        get() = "https://image.tmdb.org/t/p/w780${backdrop_path}"
}


data class MovieResponse(
    val results: List<Movie>
)

