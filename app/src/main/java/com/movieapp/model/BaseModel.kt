package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class BaseModel(@SerializedName("results") val list :ArrayList<MovieModel>? = ArrayList(),
                     @SerializedName("page") var page :Int? = 1,
                     @SerializedName("total_pages") val totalPages :Int? = 0,
                     @SerializedName("total_results") val totalResults :Int? = 0)
