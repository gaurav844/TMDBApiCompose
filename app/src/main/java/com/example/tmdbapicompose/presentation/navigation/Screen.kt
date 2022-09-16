package com.example.tmdbapicompose.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
    object MovieDetails : Screen("movie_detail_screen")
}