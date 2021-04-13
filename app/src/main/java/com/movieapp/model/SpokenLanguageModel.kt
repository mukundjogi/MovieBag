package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class SpokenLanguageModel(@SerializedName("english_name") val englishName :String? = "",
                               @SerializedName("iso_639_1") val shortNameLanguange :String? = "",
                               @SerializedName("name") val languageName :String? = "")
