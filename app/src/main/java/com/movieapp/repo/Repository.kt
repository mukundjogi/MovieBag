package com.movieapp.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.movieapp.model.BaseModel
import com.movieapp.model.MovieModel
import com.movieapp.network.ApiConstants
import com.movieapp.network.RetrofitService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository {
    private val apiClient = RetrofitService().getApiClient()

    fun getNowPlayingMovies(page: Int?): MutableLiveData<BaseModel> {
         val nowPlayingMovieList = MutableLiveData<BaseModel>()
        val call: Call<BaseModel> = apiClient!!.getNowPlaying(ApiConstants.API_KEY, "en-US", page!!)
        call.enqueue(object : Callback<BaseModel> {
            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                if (response?.body() != null)
                    nowPlayingMovieList.value = (response.body()!!)
            }

            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                nowPlayingMovieList.postValue(null)
            }
        })
        return nowPlayingMovieList
    }

    fun getPopularMovies(page: Int?): MutableLiveData<BaseModel> {
        val popularMovieList = MutableLiveData<BaseModel>()
        val call: Call<BaseModel> =
            apiClient!!.getPopularMovies(ApiConstants.API_KEY, "en-US", page!!)
        call.enqueue(object : Callback<BaseModel> {
            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                Log.e("response = ", response.toString())
                if (popularMovieList != null && response.body() != null) {
                    popularMovieList.value = (response.body()!!)
                }
            }

            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                if (popularMovieList != null)
                    popularMovieList.postValue(null)
            }
        })
        return popularMovieList
    }

    fun getMovieDetail(movieID: Int?): MutableLiveData<MovieModel> {
        val movieList = MutableLiveData<MovieModel>()
        val call: Call<JsonObject> = apiClient!!.getMovieDetail(movieID, ApiConstants.API_KEY, "en-US")
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.body() != null) {
                    Log.e("response = ", response.toString())
                    Log.e("response = ", response.body().toString())
                    val jsonObject = JSONObject(response.body().toString())
                    val gson = Gson()
                    val movieModel = gson.fromJson(jsonObject.toString(), MovieModel::class.java)
                    movieList.value = movieModel
                } else {
                     Log.e("errorBody= ", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }
        })
        return movieList
    }

    fun getSimilarMovies(movieID: Int?,page: Int?): MutableLiveData<BaseModel> {
        val popularMovieList = MutableLiveData<BaseModel>()
        val call: Call<BaseModel> =
            apiClient!!.getSimilarMovie(movieID,ApiConstants.API_KEY, "en-US", page!!)
        call.enqueue(object : Callback<BaseModel> {
            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                Log.e("response = ", response.toString())
                if (popularMovieList != null && response.body() != null) {
                    Log.e("response = ",response.body().toString())
                    popularMovieList.value = (response.body()!!)
                } else {
//                    Log.e("response = ",response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                if (popularMovieList != null)
                    popularMovieList.postValue(null)
            }
        })
        return popularMovieList
    }

    fun getMovieReviews(movieID: Int?,page: Int?): MutableLiveData<JsonObject> {
        val movieReviewList = MutableLiveData<JsonObject>()
        val call: Call<JsonObject> =
            apiClient!!.getMovieReviews(movieID,ApiConstants.API_KEY, "en-US", page!!)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>?) {
                Log.e("response = ", response.toString())
                if (movieReviewList != null && response?.body() != null) {
                    Log.e("response = ",response.body().toString())
                    movieReviewList.value = (response.body()!!)
                } else {
//                    Log.e("response = ",response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                if (movieReviewList != null)
                    movieReviewList.postValue(null)
            }
        })
        return movieReviewList
    }

    fun getMovieCast(movieID: Int?): MutableLiveData<JsonObject> {
        val movieCastList = MutableLiveData<JsonObject>()
        val call: Call<JsonObject> =
            apiClient!!.getMovieCast(movieID,ApiConstants.API_KEY, "en-US")
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>?) {
                Log.e("response = ", response.toString())
                if (movieCastList != null && response?.body() != null) {
                    Log.e("response = ",response.body().toString())
                    movieCastList.value = (response.body()!!)
                } else {
//                    Log.e("response = ",response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                if (movieCastList != null)
                    movieCastList.postValue(null)
            }
        })
        return movieCastList
    }
}