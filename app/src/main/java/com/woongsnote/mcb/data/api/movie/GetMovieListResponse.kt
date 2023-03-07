package com.woongsnote.mcb.data.api.movie

import com.google.gson.annotations.SerializedName
import com.woongsnote.mcb.data.model.Movie

data class GetMovieListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<Movie>,
    @SerializedName("total_pages") val pages: Int,
    @SerializedName("total_results") val results: Int
)
