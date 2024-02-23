package com.shaunyarbrough.laptracks.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class LapTrackViewModel: ViewModel() {
	fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
		viewModelScope.launch(
			CoroutineExceptionHandler { _, throwable ->
				Log.d(ERROR_TAG, throwable.message.orEmpty())
			},
			block = block
		)

	companion object{
		const val ERROR_TAG = "LAP APP ERROR"
	}
}