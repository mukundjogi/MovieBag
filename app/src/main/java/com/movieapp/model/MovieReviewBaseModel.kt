package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class MovieReviewBaseModel(@SerializedName("results") val list :ArrayList<MovieReviewModel>? = ArrayList(),
                                @SerializedName("page") var page :Int? = 1,
                                @SerializedName("id") var id :Int? = 0,
                                @SerializedName("total_pages") val totalPages :Int? = 0,
                                @SerializedName("total_results") val totalResults :Int? = 0)
