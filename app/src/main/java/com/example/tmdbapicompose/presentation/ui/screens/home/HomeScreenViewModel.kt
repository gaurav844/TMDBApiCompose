package com.example.tmdbapicompose.presentation.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapicompose.data.Resource
import com.example.tmdbapicompose.data.repository.HomeScreenRepository
import com.example.tmdbapicompose.domain.models.MovieResponse
import com.example.tmdbapicompose.domain.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel      //Mandatory for Hilt to Inject ViewModel
class HomeScreenViewModel
@Inject constructor( // Hilt constructor injection
    private val repository : HomeScreenRepository, // injecting HomeScreenRepository provided by AppModule
    private val logger:Logger // Injecting Independent Logger.class
    ) //Constructor Injection
    : ViewModel() {

    private val _movieRes = MutableStateFlow<Resource<MovieResponse>>(Resource.Initial)
    var movieRes: StateFlow<Resource<MovieResponse>> = _movieRes.asStateFlow()

    fun fetchAllData(page: Int) {
        logger.i("Test............")
        viewModelScope.launch {
            try {
                val res1 = async { repository.getMovieList(page) }
//                val res2 = async { repository.getMovieList(page) }

                _movieRes.value = Resource.Loading

                val resultFromApi1 = res1.await()
//                val resultFromApi2 = res2.await()

                _movieRes.value = resultFromApi1
//                _movieRes2.value = resultFromApi2
            } catch (e: Exception) {
                Log.d("LogException", e.toString())
            }
        }
    }
}