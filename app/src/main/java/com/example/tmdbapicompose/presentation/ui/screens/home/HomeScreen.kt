package com.example.tmdbapicompose.presentation.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.tmdbapicompose.presentation.ui.theme.Typography
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

const val url = "https://image.tmdb.org/t/p/w342"

@Composable
fun HomeScreen(navController: NavHostController, logger: Logger) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()

    val movieState = viewModel.movieRes.collectAsState()
    var swipeRefreshState = rememberSwipeRefreshState(isRefreshing = true)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SwipeRefresh(state = swipeRefreshState, onRefresh = { viewModel.fetchAllData(1) }) {
            LoadStateLayout(
                movieState = movieState.value,
                navController = navController,
                logger = logger
            ){
                swipeRefreshState.isRefreshing = !it
            }
        }
    }
}


@Composable
fun LoadStateLayout(
    movieState: Resource<MovieResponse>,
    navController: NavHostController,
    logger: Logger,
    onLoad:(loaded:Boolean)->Unit
) {

    when (movieState) {
        is Resource.Success -> {
            onLoad(true)
            LoadMainContent(
                movieList = movieState.value.results,
                navController = navController,
            )
        }
        is Resource.Loading -> {
            LottieLoader(R.raw.loading)
            onLoad(false)
        }
        is Resource.Failure -> {
            onLoad(false)
            Toast.makeText(LocalContext.current,"failed",Toast.LENGTH_SHORT).show()
        }
        else -> {

        }
    }

}

@Composable
fun LoadMainContent(
    movieList: List<Result>,
    navController: NavHostController
) {
        Column(Modifier.fillMaxSize()) {
            Text(
                text = "Popular Movies",
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(125.dp),
                content = {
                    itemsIndexed(movieList) { index, item ->
                        Box(
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            ItemView(item) {
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
fun ItemView(result: Result, onItemClicked: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .shadow(2.dp, RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .fillMaxSize()
            .clickable { onItemClicked() }
    ) {
        val showProgressBarState = remember { mutableStateOf(false) }
        if (showProgressBarState.value) {
            CenterCircularProgressBar()
        }
        AsyncImage(
            onLoading = {
                showProgressBarState.value = true
            },
            onSuccess = {
                showProgressBarState.value = false
            },
            model = url + result.poster_path,
            alignment = Alignment.Center,
            contentDescription = result.title,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}
