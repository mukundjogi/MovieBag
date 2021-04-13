package com.movieapp.model

import com.google.gson.annotations.SerializedName

data class MovieReviewModel(@SerializedName("author") val author :String? = "",
                            @SerializedName("author_details") val authorDetails :Any? = Any(),
                            @SerializedName("content") val content :String? = "",
                            @SerializedName("created_at") val createdAt :String? = "",
                            @SerializedName("id") val id :String? = "",
                            @SerializedName("updated_at") val updatedAt :String? = "",
                            @SerializedName("name") val name :String? = "",
                            @SerializedName("username") val username :String? = "",
                            @SerializedName("avatar_path") val avatarPath :String? = "",
                            @SerializedName("rating") val rating :Double? = 0.0,
                            @SerializedName("url") val url :String? = "")
