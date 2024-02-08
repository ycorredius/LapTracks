package com.shaunyarbrough.laptracks

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// Leaving this for testing in the future but isn't currently in use
class HiltTestRunner : AndroidJUnitRunner() {
	override fun newApplication(
		cl: ClassLoader?,
		name: String?,
		context: Context?
	): Application {
		return super.newApplication(cl, HiltTestApplication::class.java.name, context)
	}
}