package com.movieapp.repo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.movieapp.model.BaseModel
import com.movieapp.model.MovieModel


class MovieViewModel(application: Application?) : AndroidViewModel(application!!) {
    private var repository : Repository?=null

    fun getRepository(){
        repository = Repository()
    }

    fun getNowPlayingMovies(page: Int?) :MutableLiveData<BaseModel>{
        return repository?.getNowPlayingMovies(page)!!
    }

    fun getPopularMovies(page: Int?) :MutableLiveData<BaseModel>{
        return repository?.getPopularMovies(page)!!
    }

    fun getMovieDetail(movieID : Int?) : MutableLiveData<MovieModel> {
        return repository?.getMovieDetail(movieID!!)!!
    }

    fun getSimilarMovies(movieID : Int?,page: Int?) :MutableLiveData<BaseModel>{
        return repository?.getSimilarMovies(movieID,page)!!
    }

    fun getMovieReview(movieID : Int?,page: Int?) :MutableLiveData<JsonObject>{
        return repository?.getMovieReviews(movieID,page)!!
    }

    fun getMovieCast(movieID : Int?) :MutableLiveData<JsonObject>{
        return repository?.getMovieCast(movieID!!)!!
    }
}