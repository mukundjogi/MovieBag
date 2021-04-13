package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class ProductionCompaniesModel(@SerializedName("id") val ProductionId :Int? = 0,
                                    @SerializedName("logo_path") val logoPath :String? = "",
                                    @SerializedName("origin_country") val originCountry :String? = "",
                                    @SerializedName("name") val productionName :String? = "")
