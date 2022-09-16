package com.example.tmdbapicompose.data.repository


import com.example.tmdbapicompose.data.apiCall.SafeApiCall
import com.example.tmdbapicompose.data.apiCall.UserApi
import javax.inject.Inject


class HomeScreenRepository @Inject constructor(private val api: UserApi): SafeApiCall {

    suspend fun getMovieList(page : Int) = safeApiCall {
        api.getMovieList(page)
    }
}