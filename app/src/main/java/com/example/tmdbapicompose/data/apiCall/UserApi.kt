package com.example.tmdbapicompose.data.apiCall

import com.example.tmdbapicompose.domain.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An Interface which contains All the API requests
 */

interface UserApi {
    @GET("movie/popular")
    suspend fun getMovieList(@Query("page") page: Int): MovieResponse

//    @GET("movie/popular")
//    suspend fun getHoriMovieList(@Query("page") page: Int): MovieResponse
}