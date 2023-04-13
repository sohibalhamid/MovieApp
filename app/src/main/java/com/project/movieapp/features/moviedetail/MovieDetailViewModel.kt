package com.project.movieapp.features.moviedetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.movieapp.services.networks.ApiConfig
import com.project.movieapp.services.responses.ResultsItem
import com.project.movieapp.services.responses.ResultsReviewItem
import com.project.movieapp.services.responses.ReviewResponse
import com.project.movieapp.services.responses.TrailerResponse
import com.project.movieapp.utils.GlobalVariable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailViewModel : ViewModel() {
    private val _videoUrl = MutableLiveData<List<ResultsItem>>()
    val videoUrl: LiveData<List<ResultsItem>> = _videoUrl

    private val _reviews = MutableLiveData<List<ResultsReviewItem>>()
    val reviews: LiveData<List<ResultsReviewItem>> = _reviews

    fun getVideoUrl(movieId: String){
        val client = ApiConfig.getApiService().getVideoMovie(movieId, GlobalVariable.API_KEY)
        client.enqueue(object : Callback<TrailerResponse> {
            override fun onResponse(call: Call<TrailerResponse>, response: Response<TrailerResponse>) {
                if (response.isSuccessful){
                    if (response.body()?.results.isNullOrEmpty()){
                        Log.e("trailer", "Data Not Found")
                    }else{
                        _videoUrl.postValue(response.body()?.results)
                    }
                }else{
                    Log.e("trailer-resp", "OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                Log.e("trailer-throw", "OnFailure: ${t.message.toString()}")
            }
        })
    }

    fun getReviews(movieId: String, page: Int){
        val client = ApiConfig.getApiService().getReviews(movieId, GlobalVariable.API_KEY, page)
        client.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful){
                    if (response.body()?.results.isNullOrEmpty()){
                        Log.e("DetailViewModel", "Data Not Found")
                    }else{
                        _reviews.value = response.body()?.results
                    }
                }else{
                    Log.e("DetailViewModel", "OnFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Log.e("DetailViewModel", "OnFailure: ${t.message.toString()}")
            }
        })
    }
}
