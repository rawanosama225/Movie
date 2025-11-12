package com.example.myfinalproject.SearchScreen.searchUi

import androidx.compose.runtime.Composable
import com.example.myfinalproject.Model.Data.SearchCategory
import com.example.myfinalproject.Model.Data.SearchResult

@Composable
fun SearchResultsContent(
    searchResult: SearchResult,
    selectedCategory: SearchCategory,
    onMovieClick: (Int) -> Unit,
    onGenreClick: (Int) -> Unit
) {
    when (selectedCategory) {
        SearchCategory.MOVIES -> MoviesList(
            movies = searchResult.movies,
            onMovieClick = onMovieClick
        )
        SearchCategory.ACTORS -> ActorsList(
            actors = searchResult.actors
        )
        SearchCategory.GENRES -> GenresList(
            genres = searchResult.genres,
            onGenreClick = onGenreClick
        )
    }
}

