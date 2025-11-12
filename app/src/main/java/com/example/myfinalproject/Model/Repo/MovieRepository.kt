package com.example.myfinalproject.Model.Repo
import android.content.Context
import com.example.myfinalproject.Model.Data.Genre
import com.example.myfinalproject.Model.Data.Movie
import com.example.myfinalproject.Model.Data.MovieDetails
import com.example.myfinalproject.Model.Data.Person
import com.example.myfinalproject.Model.Data.toFavoriteMovie
import com.example.myfinalproject.Model.Data.toMovie
import com.example.myfinalproject.Model.Network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MovieRepository(
    private val api: ApiService,
    private val favoritesRepo: FavoritesRepository,
    context: Context
) {
    private val apiKey = "b735b576cb4511412ba7e2b0e8bb36c6"

    private val cachedMovieDetails = mutableMapOf<Int, MovieDetails>()



    private var cachedNowPlaying: List<Movie>? = null
    private var cachedPopular: List<Movie>? = null
    private var cachedTopRated: List<Movie>? = null
    private var cachedUpcoming: List<Movie>? = null


    val favoriteIds: Flow<Set<Int>> = favoritesRepo.getFavoriteIdsFlow()
        .map { it.toSet() }


    suspend fun getNowPlayingMovies(): List<Movie> {
        return try {
            val all = mutableListOf<Movie>()
            for (page in 1..3) {
                val response = api.getNowPlayingMovies(apiKey, page)
                all.addAll(response.results)
            }

            cachedNowPlaying = all.distinctBy { it.id }
            cachedNowPlaying!!
        } catch (e: Exception) {
            e.printStackTrace()
            cachedNowPlaying ?: emptyList()
        }
    }


    suspend fun getPopularMovies(): List<Movie> {
        return try {
            val movies = api.getPopularMovies(apiKey).results

            cachedPopular = movies.distinctBy { it.id }
            cachedPopular!!
        } catch (e: Exception) {
            e.printStackTrace()
            cachedPopular ?: emptyList()
        }
    }


    suspend fun getTopRatedMovies(): List<Movie> {
        return try {
            val movies = api.getTopRatedMovies(apiKey).results

            cachedTopRated = movies.distinctBy { it.id }
            cachedTopRated!!
        } catch (e: Exception) {
            e.printStackTrace()
            cachedTopRated ?: emptyList()
        }
    }


    suspend fun getUpcomingMovies(): List<Movie> {
        return try {
            val movies = api.getUpcomingMovies(apiKey).results

            cachedUpcoming = movies.distinctBy { it.id }
            cachedUpcoming!!
        } catch (e: Exception) {
            e.printStackTrace()
            cachedUpcoming ?: emptyList()
        }
    }

    suspend fun fetchMovieDetails(movieId: Int): MovieDetails {
        return api.getMovieDetails(movieId, apiKey)
    }

    fun cacheMovieDetails(movie: MovieDetails) {
        cachedMovieDetails[movie.id] = movie
    }

    fun getCachedMovieDetails(movieId: Int): MovieDetails? {
        return cachedMovieDetails[movieId]
    }

    // ============== FAVORITES ==============

    suspend fun getFavoriteMovies(): List<Movie> {
        return try {
            favoritesRepo.getAllFavorites().map { it.toMovie() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addToFavorites(movie: Movie) {
        try {
            favoritesRepo.addFavorite(movie.toFavoriteMovie())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeFromFavorites(movieId: Int) {
        try {
            favoritesRepo.removeFavorite(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return try {
            favoritesRepo.isFavorite(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return try {
            val movies = api.searchMovies(apiKey, query).results

            movies.distinctBy { it.id }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchPeople(query: String): List<Person> {
        return try {
            api.searchPeople(apiKey, query).results
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getGenres(): List<Genre> {
        return try {
            api.getGenres(apiKey).genres
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getMoviesByGenre(genreId: Int): List<Movie> {
        return try {
            val movies = api.getMoviesByGenre(apiKey, genreId).results

            movies.distinctBy { it.id }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


}
