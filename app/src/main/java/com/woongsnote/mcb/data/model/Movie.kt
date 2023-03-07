package com.woongsnote.mcb.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("backdrop_path") val backPath: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    )