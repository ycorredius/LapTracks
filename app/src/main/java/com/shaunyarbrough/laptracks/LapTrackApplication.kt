package com.shaunyarbrough.laptracks

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //If I add this is breaks. The Error message is not very descriptive
class LapTrackApplication : Application() {
	companion object{
		lateinit var instance: LapTrackApplication
			private set
	}

	override fun onCreate() {
		super.onCreate()
		instance = this
	}
}
