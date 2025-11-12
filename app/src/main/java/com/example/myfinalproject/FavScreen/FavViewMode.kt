package com.example.myfinalproject.FavScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfinalproject.Model.Repo.MovieRepository
import com.example.myfinalproject.Model.Data.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel()

{

    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies = _favoriteMovies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        //  loadFavorites
        observeFavorites()
    }


    private fun observeFavorites() {
        viewModelScope.launch {
            repository.favoriteIds.collect {
                loadFavorites()
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _favoriteMovies.value = repository.getFavoriteMovies()
            } catch (e: Exception) {
                e.printStackTrace()
                _favoriteMovies.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            repository.removeFromFavorites(movieId)
           // loadFavorites() // Reload the list
        }
    }
}

class FavoritesViewModelFactory(
    private val repository: MovieRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
