package com.shaunyarbrough.laptracks

import android.app.Application
import com.shaunyarbrough.laptracks.data.AppContainer
import com.shaunyarbrough.laptracks.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //If I add this is breaks. The Error message is not very descriptive
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
