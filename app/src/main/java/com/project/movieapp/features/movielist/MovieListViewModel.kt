package com.project.movieapp.features.movielist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.movieapp.services.networks.ApiConfig
import com.project.movieapp.services.responses.GenreResponse
import com.project.movieapp.services.responses.MovieListResponse
import com.project.movieapp.utils.GlobalVariable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewModel : ViewModel() {
    private val _isLoadingListGenre = MutableLiveData<Boolean>()
    val isLoadingListGenre: MutableLiveData<Boolean> = _isLoadingListGenre

    private val _genreResponse = MutableLiveData<GenreResponse>()
    val genreResponse: LiveData<GenreResponse> = _genreResponse

    private val _isLoadingListMovie = MutableLiveData<Boolean>()
    val isLoadingListMovie: MutableLiveData<Boolean> = _isLoadingListMovie

    private val _movieList = MutableLiveData<MovieListResponse>()
    val movieResponse: LiveData<MovieListResponse> = _movieList

    fun getGenreList() {
        _isLoadingListGenre.postValue(true)
        val client = ApiConfig.getApiService().getGenreMovie()
        client.enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                _isLoadingListGenre.postValue(false)
                if (response.isSuccessful){
                    _genreResponse.postValue(response.body())
                }else{
                    Log.e("genre-resp", "OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                _isLoadingListGenre.postValue(false)
                Log.e("genre-throw", "OnFailure: ${t.message}")
            }
        })
    }

    fun getMovieList(genreId: String, page: Int) {
        _isLoadingListMovie.postValue(true)
        val client = ApiConfig.getApiService().getMovies(GlobalVariable.API_KEY, genreId, page)
        client.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(call: Call<MovieListResponse>, response: Response<MovieListResponse>) {
                _isLoadingListMovie.postValue(false)
                if (response.isSuccessful){
                    _movieList.postValue(response.body())
                }else{
                    Log.e("movie-resp", "OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                _isLoadingListMovie.postValue(false)
                Log.e("movie-throw", "OnFailure: ${t.message.toString()}")
            }
        })
    }
}
