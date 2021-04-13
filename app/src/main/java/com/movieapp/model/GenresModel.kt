package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class GenresModel(@SerializedName("id") val genreId :Int? = 0,
                       @SerializedName("name") val genreName :String? = "")
