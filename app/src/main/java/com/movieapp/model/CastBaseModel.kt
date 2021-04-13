package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class CastBaseModel (
	@SerializedName("id") val id : Int,
	@SerializedName("cast") val cast : List<MovieCastModel>,
	@SerializedName("crew") val crew : List<MovieCrewModel>
)