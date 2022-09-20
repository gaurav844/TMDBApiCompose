package com.example.tmdbapicompose.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tmdbapicompose.R
import com.example.tmdbapicompose.data.Resource
import com.example.tmdbapicompose.domain.models.MovieResponse
import com.example.tmdbapicompose.domain.models.Result
import com.example.tmdbapicompose.domain.utils.Logger
import com.example.tmdbapicompose.domain.utils.superNavigate
import com.example.tmdbapicompose.presentation.navigation.Screen
import com.example.tmdbapicompose.presentation.ui.customComposables.CenterCircularProgressBar
import com.example.tmdbapicompose.presentation.ui.customComposables.LottieLoader

val url = "https://image.tmdb.org/t/p/w342"

@Composable
fun HomeScreen(navController: NavHostController,viewModel: HomeScreenViewModel,logger:Logger) {
    val showProgressBarState = remember { mutableStateOf(false) }
    val movies: State<MutableList<Result>> = remember { mutableStateOf(mutableListOf())}
    if (showProgressBarState.value) LottieLoader(R.raw.loading)
    else LoadMainContent(movies,navController)

    setupObservable(viewModel){
        when (it){
            is Resource.Success->{
                showProgressBarState.value = false
                movies.value.addAll(it.value.results)
                logger.i("response added in list")
            }
            is Resource.Loading ->{
                showProgressBarState.value = true
            }
            is Resource.Failure ->{
                showProgressBarState.value = false
            }
            else -> {
                showProgressBarState.value = false
            }
        }
    }
}



@Composable
fun LoadMainContent(movies: State<MutableList<Result>>, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(125.dp),
            content = {
                itemsIndexed(movies.value){index, item ->
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        ItemView(item){
                            navController.apply {
                                currentBackStackEntry?.savedStateHandle?.set(
                                    key = "result",
                                    value = item
                                )

                                superNavigate(Screen.MovieDetails.route)
                            }

                            //navController.navigate("details/$title/$release_date$poster_path/$original_language/${vote_average.toString()}/$overview")
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ItemView(result: Result,onItemClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .fillMaxSize()
            .clickable {onItemClicked() }
    ) {
        val showProgressBarState = remember { mutableStateOf(false) }
        if (showProgressBarState.value) { CenterCircularProgressBar() }
        AsyncImage(
            onLoading = {
                showProgressBarState.value = true
            },
            onSuccess = {
                showProgressBarState.value = false
            },
            model = url+result.poster_path,
            alignment = Alignment.Center,
            contentDescription = result.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}



@Composable
fun setupObservable(
    viewModel: HomeScreenViewModel,
    onResponse:(res:Resource<MovieResponse>) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchAllData(1)
        viewModel.movieRes.collect{
            onResponse(it)
        }
    }
}