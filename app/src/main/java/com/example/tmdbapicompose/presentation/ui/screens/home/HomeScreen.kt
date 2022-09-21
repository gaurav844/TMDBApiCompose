package com.example.tmdbapicompose.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val movieState = viewModel.movieRes.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LoadMainContent(
            movieState = movieState.value,
            navController = navController,
            logger = logger
        )
    }
}



@Composable
fun LoadMainContent(movieState: Resource<MovieResponse>, navController: NavHostController, logger: Logger) {
    when (movieState){
        is Resource.Success->{
            LazyVerticalGrid(
                columns = GridCells.Adaptive(125.dp),
                content = {
                    itemsIndexed(movieState.value.results){index, item ->
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
        is Resource.Loading ->{
            LottieLoader(R.raw.loading)
        }
        is Resource.Failure ->{

        }
        else -> {

        }
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
            .clickable { onItemClicked() }
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
