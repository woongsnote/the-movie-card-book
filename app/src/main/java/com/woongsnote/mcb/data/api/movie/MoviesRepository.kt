package com.woongsnote.mcb.data.api.movie

import com.woongsnote.mcb.data.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {
    private val movieApi: MovieApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        movieApi = retrofit.create(MovieApi::class.java)
    }

    fun getSearchMovies(query:String, onSuccess: (movies:List<Movie>) -> Unit, onError:()-> Unit){

        movieApi.getSearchMovies(query = query).enqueue(
            object : Callback<GetMovieListResponse>{
                override fun onResponse(
                    call: Call<GetMovieListResponse>,
                    response: Response<GetMovieListResponse>
                ) {
                    if (response.isSuccessful){

                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        }else{
                            onError.invoke()
                        }
                    }
                }

                override fun onFailure(call: Call<GetMovieListResponse>, t: Throwable) {
                    onError.invoke()
                }

            }
        )
    }

}