package com.example.myfinalproject.Model.Data

data class PeopleResponse(
    val results: List<Person>
)

data class Person(
    val id: Int,
    val name: String,
    val profile_path: String?,
    val known_for_department: String?,
    val known_for: List<Movie>
) {
    val profileUrl: String
        get() = "https://image.tmdb.org/t/p/w500${profile_path}"
}

data class Genre(
    val id: Int,
    val name: String
)
data class GenreResponse(
    val genres: List<Genre>
)
