package com.example.laptracks

import android.app.Application
import com.example.laptracks.data.AppContainer
import com.example.laptracks.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

class LapTrackApplication : Application() {

  /**
   * AppContainer instance used by the rest of classes to obtain dependencies
   */
  lateinit var container: AppContainer

  override fun onCreate() {
    super.onCreate()
    container = AppDataContainer(this)
  }
}
