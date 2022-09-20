package com.example.tmdbapicompose.data

import okhttp3.ResponseBody

/**
 * Interface for fixed type hierarchy
 * for handling http request response
 * (useful to prevent creating new subclasses)
 */

sealed interface Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>
    object Loading : Resource<Nothing>
    object Initial : Resource<Nothing>
}