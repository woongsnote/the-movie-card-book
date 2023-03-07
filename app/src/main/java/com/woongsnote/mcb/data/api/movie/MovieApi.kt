package com.woongsnote.mcb.data.api.movie

import com.woongsnote.mcb.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("search/movie")
    fun getSearchMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("query") query: String = "",
        @Query("language") language: String = "ko"
    ) : Call<GetMovieListResponse>
}