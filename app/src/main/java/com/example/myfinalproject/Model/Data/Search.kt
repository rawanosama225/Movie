package com.example.myfinalproject.Model.Data


enum class SearchCategory {
    MOVIES, ACTORS, GENRES
}

sealed class SearchUiState {
    object Initial : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val searchResult: SearchResult) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

data class SearchResult(
    val movies: List<Movie> = emptyList(),
    val actors: List<Person> = emptyList(),
    val genres: List<Genre> = emptyList()
)