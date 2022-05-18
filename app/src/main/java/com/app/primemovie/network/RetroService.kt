package com.app.primemovie.network


import com.app.primemovie.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {

    @GET("top_rated")
    fun getTopRatedMovie(@Query("api_key") query: String): Call<MovieResponse>


    @GET("popular")
    fun getPopular(@Query("api_key") query: String): Call<MovieResponse>
}