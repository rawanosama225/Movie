package com.example.myfinalproject.Model.Network

import com.example.myfinalproject.Model.Data.GenreResponse
import com.example.myfinalproject.Model.Data.MovieDetails
import com.example.myfinalproject.Model.Data.MovieResponse
import com.example.myfinalproject.Model.Data.PeopleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
      //  @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
       @Query("api_key") apiKey: String = "b735b576cb4511412ba7e2b0e8bb36c6",
        @Query("language") language: String = "en-US"
    ): MovieDetails

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("search/person")
    suspend fun searchPeople(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): PeopleResponse

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1
    ): MovieResponse

//    @GET("person/{person_id}")
//    suspend fun getPersonDetails(
//        @Path("person_id") personId: Int,
//        @Query("api_key") apiKey: String,
//        @Query("append_to_response") appendToResponse: String = "movie_credits"
//    ): PersonDetails
}



