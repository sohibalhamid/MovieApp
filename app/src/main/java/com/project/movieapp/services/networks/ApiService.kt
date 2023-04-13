package com.project.movieapp.services.networks

import com.project.movieapp.services.responses.GenreResponse
import com.project.movieapp.services.responses.MovieListResponse
import com.project.movieapp.services.responses.ReviewResponse
import com.project.movieapp.services.responses.TrailerResponse
import com.project.movieapp.utils.GlobalVariable.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list?api_key=$API_KEY")
    fun getGenreMovie(): Call<GenreResponse>

    @GET("discover/movie")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String,
        @Query("page") page: Int
    ): Call<MovieListResponse>

    @GET("movie/{movie_id}/videos")
    fun getVideoMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String
    ): Call<TrailerResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviews(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Call<ReviewResponse>
}
