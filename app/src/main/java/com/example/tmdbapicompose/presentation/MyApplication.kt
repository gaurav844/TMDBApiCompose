package com.example.tmdbapicompose.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp to trigger Hilt Code Generation
 * (application-level dependency container)
 */
@HiltAndroidApp
class MyApplication:Application()