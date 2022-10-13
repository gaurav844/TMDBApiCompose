package com.example.tmdbapicompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tmdbapicompose.R
import com.example.tmdbapicompose.domain.models.Result
import com.example.tmdbapicompose.domain.utils.Logger
import com.example.tmdbapicompose.domain.utils.NetworkUtil
import com.example.tmdbapicompose.presentation.navigation.Screen
import com.example.tmdbapicompose.presentation.ui.customComposables.LottieLoader
import com.example.tmdbapicompose.presentation.ui.screens.AnimatedSplashScreen
import com.example.tmdbapicompose.presentation.ui.screens.home.HomeScreen
import com.example.tmdbapicompose.presentation.ui.screens.movieDetail.MovieDetailScreen
import com.example.tmdbapicompose.presentation.ui.theme.TMDBApiComposeTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *  @AndroidEntryPoint require to use dependencies provided by Hilt
 *  If you annotate an Android class with @AndroidEntryPoint, then you also must annotate Android classes that depend on it.
 *  For example, if you annotate a fragment, then you must also annotate any activities where you use that fragment.
 */

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkUtil: NetworkUtil // Hilt Field Injection
    @Inject
    lateinit var logger: Logger // Hilt Field Injection without creating dependency in @Module

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TMDBApiComposeTheme {
                val navController = rememberAnimatedNavController()
                var isNetwork by remember { mutableStateOf(true) }
                networkUtil.observe(this) { isNetwork = it }

                if (isNetwork) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route
                    ) {
                        composable(
                            route = Screen.Splash.route,
                            exitTransition = {     //Animation for home screen while navigating to detail screen
                                slideOutHorizontally(
                                    targetOffsetX = { -300 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(animationSpec = tween(300))
                            },
                        ) {
                            AnimatedSplashScreen(navController = navController)
                        }
                        composable(
                            route = Screen.Home.route,
                            enterTransition = {
                                              slideInHorizontally(
                                                  animationSpec = tween(
                                                      durationMillis = 300,
                                                      easing = FastOutSlowInEasing
                                                  )
                                              ) + fadeIn(animationSpec = tween(300))
                            },
                            exitTransition = {     //Animation for home screen while navigating to detail screen
                                slideOutHorizontally(
                                    targetOffsetX = { -300 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(animationSpec = tween(300))
                            },
                            popEnterTransition = {   //Animation for home screen while pop navigating back from detail screen
                                slideInHorizontally(
                                    initialOffsetX = { -300 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(animationSpec = tween(300))
                            }
                        ) {
                            HomeScreen(navController = navController, logger = logger)
                        }
                        composable(
                            route = Screen.MovieDetails.route,
                            enterTransition = {   //Animation for detail screen while navigating from home screen
                                slideInHorizontally(
                                    initialOffsetX = { 300 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeIn(animationSpec = tween(300))
                            },
                            popExitTransition = {   //Animation for detail screen while pop navigating back to home screen
                                slideOutHorizontally(
                                    targetOffsetX = { 300 },
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                ) + fadeOut(animationSpec = tween(300))
                            }
                        ) {
                            var result: Result? =
                                navController.previousBackStackEntry?.savedStateHandle?.get<Result>(
                                    "result"
                                )
                            MovieDetailScreen(result = result)
                        }
                    }

                } else InfoScreen(
                    lottieRes = R.raw.offline_lottie,
                    title = "You seem to be offline",
                    desc = "please check your internet connection and try again",
                    actionText = "Reload Page"
                )

            }
        }
    }

    @Composable
    fun InfoScreen(
        lottieRes: Int,
        title: String,
        desc: String,
        actionText: String
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieLoader(raw = lottieRes, modifier = Modifier
                .width(200.dp)
                .height(200.dp))
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,

                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = desc, modifier = Modifier
                    .fillMaxWidth(0.75f), textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = actionText,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
