package com.example.tmdbapicompose.presentation.ui.screens.home

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
import kotlinx.coroutines.flow.update
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

    init {
        fetchAllData(1)
    }

    fun fetchAllData(page: Int) {
        logger.i("Test............")
        viewModelScope.launch {
            try {
                _movieRes.update { Resource.Loading }

                val res1 = async { repository.getMovieList(page) }
//                val res2 = async { repository.getMovieList(page) }

                val resultFromApi1 = res1.await()
//                val resultFromApi2 = res2.await()
                _movieRes.update { resultFromApi1 }
//                _movieRes2.value = resultFromApi2
            } catch (exception: Exception) {
                logger.e("LogException", exception)
            }
        }
    }
}