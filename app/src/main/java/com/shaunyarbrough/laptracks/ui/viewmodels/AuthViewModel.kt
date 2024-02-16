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

	fun login(navController: NavController) {
		viewModelScope.launch {
			if (validateAuth()) {
				val result = authRepository.loginUser(authUiState.auth.email, authUiState.auth.password)
				if (result.isSuccessful) {
					navController.navigate(ParticipantDestination.route)
				} else {
					authUiState.copy(hasError = !result.isSuccessful)
				}
			}
		}
	}

	fun updateUiState(authDetails: AuthDetails, hasError: Boolean = authUiState.hasError){
		authUiState = AuthUiState(auth = authDetails)
	}
	private fun validateAuth(authDetails: AuthDetails = authUiState.auth): Boolean {
		return authDetails.email.isNotEmpty() && authDetails.password.isNotEmpty()
	}

	data class AuthUiState(
		val hasError: Boolean = false,
		val auth: AuthDetails = AuthDetails()
	)

	data class AuthDetails(
		val email: String = "",
		val password: String = ""
	)
}
