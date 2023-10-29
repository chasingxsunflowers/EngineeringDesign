package com.example.coffeedivider

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BLEApplication : Application() {
    companion object {
        var globalVar = ""
    }
}
