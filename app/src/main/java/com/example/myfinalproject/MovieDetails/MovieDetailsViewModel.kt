package com.example.myfinalproject.MovieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfinalproject.Model.Data.Movie
import com.example.myfinalproject.Model.Repo.MovieRepository
import com.example.myfinalproject.Model.Data.MovieDetails
import com.example.myfinalproject.Model.Repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class MovieDetailsViewModel(
    private val repository: MovieRepository,
    private val userRepo: UserRepo
) : ViewModel() {

    private val _movieState = MutableStateFlow<MovieDetails?>(null)
    val movieState: StateFlow<MovieDetails?> = _movieState

    val isFavorite = MutableStateFlow(false)

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    init {

       // observeFavoriteChanges()
        getUserId()
    }
    private fun getUserId() {
        viewModelScope.launch {
            val uid = userRepo.getUserId()
            _userId.value = uid
            if (uid.isNotEmpty()) {
                observeFavoriteChanges(uid) 
            }
        }
    }

    private fun observeFavoriteChanges(userId:String) {
        viewModelScope.launch {
            repository.getFavoriteIds(userId).collect { ids ->
                val currentId = _movieState.value?.id
                if (currentId != null) {
                    isFavorite.value = ids.contains(currentId)
                }
            }
        }
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = repository.fetchMovieDetails(movieId)
                _movieState.value = movie
                isFavorite.value = repository.isFavorite(movieId)
                repository.cacheMovieDetails(movie)
            } catch (e: Exception) {
                e.printStackTrace()
                _movieState.value = repository.getCachedMovieDetails(movieId)
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val movieDetails = _movieState.value ?: return@launch
            val userId = userRepo.getUserId()

            if (isFavorite.value) {
                repository.removeFromFavorites(movieDetails.id)
                isFavorite.value = false
            } else {
                val movie = Movie(
                    id = movieDetails.id,
                    userId = userId,
                    title = movieDetails.title,
                    overview = movieDetails.overview,
                    poster_path = movieDetails.poster_path,
                    backdrop_path = movieDetails.backdrop_path,
                    release_date = movieDetails.release_date,
                    vote_average = movieDetails.vote_average
                )
                repository.addToFavorites(movie)
                isFavorite.value = true
            }
        }
    }
}

class MovieDetailsViewModelFactory(
    private val repository: MovieRepository,
    private val userRepo: UserRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(repository, userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
