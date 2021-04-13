package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class ProductionCountriesModel(@SerializedName("iso_3166_1") val shortNameLanguange :String? = "",
                                    @SerializedName("name") val countryName :String? = "")
