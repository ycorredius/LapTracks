package com.shaunyarbrough.laptracks.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.AuthRepository
import com.shaunyarbrough.laptracks.ui.views.LoginDestination
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val authRepository: AuthRepository
): ViewModel() {
	private val _accessToken: MutableStateFlow<String> = MutableStateFlow("")
	val accessToken: StateFlow<String> get() = _accessToken

	//Stateflow used to display a loading screen before access token is retrieved
	private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading

	//Set initial destination to login destination
	private val _currentDestination: MutableStateFlow<String> = MutableStateFlow(LoginDestination.route)
	val currentDestination: StateFlow<String> get() = _currentDestination

	//init used to retrieve the access token from data store if one exists
	init {
		Log.d("MainViewModel", "ViewModel initialized")
		viewModelScope.launch {
			try {
				authRepository.accessTokenFlow.collect{
						accessToken ->
					Log.d("MainViewModel", "Access Token collected: $accessToken")
					_accessToken.value = accessToken
					_isLoading.value = false
					updateDestination()
				}
			} catch (e: Exception){
				e.printStackTrace()
			}
		}
	}

	private fun updateDestination(){
		_currentDestination.value = ParticipantDestination.route
		//Until I have server issue figured out default to participant route.
//			if (accessToken.value.isNotEmpty()){
//			ParticipantDestination.route
//		} else{
//			LoginDestination.route
//		}
	}
}