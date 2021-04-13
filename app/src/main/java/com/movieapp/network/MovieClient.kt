package com.movieapp.network

import com.google.gson.JsonObject
import com.movieapp.model.BaseModel
import com.movieapp.model.MovieReviewBaseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieClient {
    @GET("3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Call<BaseModel>


    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Call<BaseModel>

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movie_id : Int?,
        @Query("api_key") api_key: String?,
        @Query("language") language: String?
    ): Call<JsonObject>

    @GET("3/movie/{movie_id}/similar")
    fun getSimilarMovie(
        @Path("movie_id") movie_id : Int?,
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Call<BaseModel>

    @GET("3/movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") movie_id : Int?,
        @Query("api_key") api_key: String?,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Call<JsonObject>

    @GET("3/movie/{movie_id}/credits")
    fun getMovieCast(
        @Path("movie_id") movie_id : Int?,
        @Query("api_key") api_key: String?,
        @Query("language") language: String?
    ): Call<JsonObject>
}