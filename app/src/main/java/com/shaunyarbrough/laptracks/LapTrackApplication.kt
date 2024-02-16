package com.shaunyarbrough.laptracks

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //If I add this is breaks. The Error message is not very descriptive
class LapTrackApplication : Application() {

}
