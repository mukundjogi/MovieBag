package com.movieapp.model

import androidx.annotation.Nullable
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class MovieModel(@SerializedName("adult") val adult: Boolean? = false,
                      @SerializedName("backdrop_path") val backdropPath:  String? = "",
                      @SerializedName("genre_ids") val genreIds: Any? = Any(),
                      @SerializedName("id") val movieId: Int? = 0,
                      @SerializedName("original_language") val originalLanguage:  String? = "",
                      @SerializedName("original_title") val originalTitle:  String? = "",
                      @SerializedName("overview") val overview:  String? = "",
                      @SerializedName("popularity") val popularity: Double? = 0.0,
                      @SerializedName("poster_path") val imagePath:  String? = "",
                      @SerializedName("release_date") val releaseDate:  String? = "",
                      @SerializedName("title") val title:  String? = "",
                      @SerializedName("name") val movieName:  String? = "",
                      @SerializedName("video") val video: Boolean? = false,
                      @SerializedName("vote_average") val voteAverage: Double? = 0.0,
                      @SerializedName("vote_count") val voteCount: Int? = 0,
                      @SerializedName("belongs_to_collection") val belongsToCollection: Any? = Any(),
                      @SerializedName("budget") val budget: Double? = 0.0,
                      @SerializedName("homepage") val homepage:  String? = "",
                      @SerializedName("imdb_id") val imdbId:  String? = "",
                      @SerializedName("production_companies") val productionCompanies: ArrayList<ProductionCompaniesModel>? = ArrayList(),
                      @SerializedName("production_countries") val productionCountries: ArrayList<ProductionCountriesModel>? = ArrayList(),
                      @SerializedName("revenue") val revenue: Double? = 0.0,
                      @SerializedName("runtime") val runtime: Double? = 0.0,
                      @SerializedName("spoken_languages") val spokenLanguages: Any? = Any(),
                      @SerializedName("status") val status:  String? = "",
                      @SerializedName("tagline") val tagline:  String? = "")
