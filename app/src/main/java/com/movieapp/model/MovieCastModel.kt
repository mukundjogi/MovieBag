package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class MovieCastModel (
	@SerializedName("adult") val adult : Boolean,
	@SerializedName("gender") val gender : Int,
	@SerializedName("id") val id : Int,
	@SerializedName("known_for_department") val knownForDepartment : String,
	@SerializedName("name") val name : String,
	@SerializedName("original_name") val originalName : String,
	@SerializedName("popularity") val popularity : Double,
	@SerializedName("profile_path") val profilePath : String,
	@SerializedName("cast_id") val castId : Int,
	@SerializedName("character") val character : String,
	@SerializedName("credit_id") val creditId : String,
	@SerializedName("order") val order : Int
)