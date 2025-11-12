package com.example.myfinalproject.Model.Data

data class MovieDetails(

    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double,
    val poster_path: String?,
    val backdrop_path: String?,
    val genres: List<Genre>
)

//data class PersonDetails(
//    val id: Int,
//    val name: String,
//    val profile_path: String?,
//    val biography: String?,
//    val birthday: String?,
//    val place_of_birth: String?,
//    val known_for_department: String?,
//    val movie_credits: MovieCredits?
//) {
//    val profileUrl: String
//        get() = "https://image.tmdb.org/t/p/w500${profile_path}"
//}
//
//data class MovieCredits(
//    val cast: List<Movie>
//)