package com.example.myfinalproject.FavScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myfinalproject.Model.Repo.MovieRepository
import com.example.myfinalproject.Model.Data.Movie
import com.example.myfinalproject.Model.Repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: MovieRepository,
    private val userRepo: UserRepo
) : ViewModel()

{

    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies = _favoriteMovies.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        //  loadFavorites
      //  observeFavorites()
        getUserId()
    }


    private fun observeFavorites(userId: String) {
        viewModelScope.launch {
            repository.getFavoriteIds(userId).collect {
                loadFavorites(userId)
            }
        }
    }

    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _favoriteMovies.value = repository.getFavoriteMovies(userId)
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

    fun getUserId(){
        viewModelScope.launch {
          // _userId.value = userRepo.getUserId()
            val uid =userRepo.getUserId()
            _userId.value =uid
            if (uid.isNotEmpty()) {

                observeFavorites(uid)
            }
        }
    }
}

class FavoritesViewModelFactory(
    private val repository: MovieRepository,
    private val userRepo: UserRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository,userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
