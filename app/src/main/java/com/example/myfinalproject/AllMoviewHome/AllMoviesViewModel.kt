package com.example.myfinalproject.AllMoviewHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfinalproject.Model.Data.Movie
import com.example.myfinalproject.Model.Repo.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val nowPlaying: List<Movie> = emptyList(),
        val popular: List<Movie> = emptyList(),
        val topRated: List<Movie> = emptyList(),
        val upcoming: List<Movie> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        observeFavoriteChanges()
    }


    private fun observeFavoriteChanges() {
        viewModelScope.launch {
            repository.favoriteIds.collect { favoriteIds ->
                val currentState = _uiState.value
                if (currentState is HomeUiState.Success) {
                    _uiState.value = currentState.copy(
                        nowPlaying = updateMoviesFavoriteState(currentState.nowPlaying, favoriteIds),
                        popular = updateMoviesFavoriteState(currentState.popular, favoriteIds),
                        topRated = updateMoviesFavoriteState(currentState.topRated, favoriteIds),
                        upcoming = updateMoviesFavoriteState(currentState.upcoming, favoriteIds)
                    )
                }
            }
        }
    }


    private fun updateMoviesFavoriteState(movies: List<Movie>, favoriteIds: Set<Int>): List<Movie> {
        return movies.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
    }

    fun fetchMovies(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _uiState.value = HomeUiState.Loading
            }

            try {
                val nowPlaying = async { repository.getNowPlayingMovies() }
                val popular = async { repository.getPopularMovies() }
                val topRated = async { repository.getTopRatedMovies() }
                val upcoming = async { repository.getUpcomingMovies() }

                val results = awaitAll(nowPlaying, popular, topRated, upcoming)


                val favoriteIds = repository.favoriteIds.first()

                _uiState.value = HomeUiState.Success(
                    nowPlaying = updateMoviesFavoriteState(results[0] as List<Movie>, favoriteIds),
                    popular = updateMoviesFavoriteState(results[1] as List<Movie>, favoriteIds),
                    topRated = updateMoviesFavoriteState(results[2] as List<Movie>, favoriteIds),
                    upcoming = updateMoviesFavoriteState(results[3] as List<Movie>, favoriteIds)
                )
            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Showing cached data."
                    is java.net.SocketTimeoutException -> "Connection timeout. Please try again."
                    else -> "Failed to load movies: ${e.message}"
                }

                val currentState = _uiState.value
                if (currentState is HomeUiState.Success &&
                    (currentState.nowPlaying.isNotEmpty() || currentState.popular.isNotEmpty())) {
                    _uiState.value = currentState
                } else {
                    _uiState.value = HomeUiState.Error(errorMessage)
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            if (movie.isFavorite) {
                repository.removeFromFavorites(movie.id)
            } else {
                repository.addToFavorites(movie)
            }
        }
    }

    //TODO: add user id to add to favorite
    fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                repository.removeFromFavorites(movieId)
            } else {
                val currentState = _uiState.value
                if (currentState is HomeUiState.Success) {
                    val movie = listOf(
                        currentState.nowPlaying,
                        currentState.popular,
                        currentState.topRated,
                        currentState.upcoming
                    ).flatten().find {
                        it.id == movieId
                    }
                    movie?.let { repository.addToFavorites(it) }
                }
            }
        }
    }

    fun initializeIfNeeded() {
        if (uiState.value == HomeUiState.Loading) {
            fetchMovies()
        }
    }

    fun retry() {
        fetchMovies()
    }
}
class HomeViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
