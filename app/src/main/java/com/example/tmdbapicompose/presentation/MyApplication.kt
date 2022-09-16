package com.example.tmdbapicompose.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp                     // Mandatory for using Hilt
class MyApplication:Application()