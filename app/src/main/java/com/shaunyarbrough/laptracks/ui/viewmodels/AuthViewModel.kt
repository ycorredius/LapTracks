package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.shaunyarbrough.laptracks.data.AuthRepository
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val authRepository: AuthRepository
) : ViewModel() {
	var authUiState by mutableStateOf(AuthUiState())
		private set

	fun login(email: String, password: String, navController: NavController) {
		viewModelScope.launch {
			if (validateAuth(email,password)) {
				val result = authRepository.loginUser(email, password)
				if (result.isSuccessful) {
					navController.navigate(ParticipantDestination.route)
				} else {
					// Reconfigure this to set error message from api
					authUiState.copy(hasError = !result.isSuccessful)
				}
			}
		}
	}
	//There is currently no error state fully functional. Do this after signup completed.
	fun signup(email: String, password: String, navController: NavController){
		viewModelScope.launch {
			if (validateAuth(email,password)){
				val result = authRepository.signupUser(email,password)
				if (result.isSuccessful){
					navController.navigate(ParticipantDestination.route)
				}else{
					//Same as above reconfigure to set error message api call
					authUiState.copy(hasError = !result.isSuccessful)
				}
			}
		}
	}

	private fun validateAuth(email: String, password: String): Boolean {
		return email.isNotEmpty() && password.isNotEmpty()
	}

	data class AuthUiState(
		val hasError: Boolean = false,
	)
}
