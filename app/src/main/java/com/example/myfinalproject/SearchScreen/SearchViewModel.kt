package com.example.myfinalproject.SearchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfinalproject.Model.Data.Genre
import com.example.myfinalproject.Model.Data.SearchCategory
import com.example.myfinalproject.Model.Data.SearchResult
import com.example.myfinalproject.Model.Data.SearchUiState
import com.example.myfinalproject.Model.Repo.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(SearchCategory.MOVIES)
    val selectedCategory: StateFlow<SearchCategory> = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    init {
        loadGenres()
    }

    private fun loadGenres() {
        viewModelScope.launch {
            _genres.value = repository.getGenres()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Initial
        }
    }

    fun onCategorySelected(category: SearchCategory) {
        _selectedCategory.value = category
        if (_searchQuery.value.isNotBlank()) {
            search()
        }
    }

    fun search() {
        val query = _searchQuery.value
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Initial
            return
        }

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            try {
                when (_selectedCategory.value) {
                    SearchCategory.MOVIES -> {
                        val movies = repository.searchMovies(query)
                        _uiState.value = SearchUiState.Success(
                            SearchResult(movies = movies)
                        )
                    }
                    SearchCategory.ACTORS -> {
                        val actors = repository.searchPeople(query)
                        _uiState.value = SearchUiState.Success(
                            SearchResult(actors = actors)
                        )
                    }
                    SearchCategory.GENRES -> {
                        val filteredGenres = _genres.value.filter {
                            it.name.contains(query, ignoreCase = true)
                        }
                        _uiState.value = SearchUiState.Success(
                            SearchResult(genres = filteredGenres)
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onGenreClick(genreId: Int) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val movies = repository.getMoviesByGenre(genreId)
                _selectedCategory.value = SearchCategory.MOVIES
                _uiState.value = SearchUiState.Success(
                    SearchResult(movies = movies)
                )
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(
                    e.message ?: "Error loading movies"
                )
            }
        }
    }
}

// ViewModelFactory
class SearchViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
