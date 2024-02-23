package com.shaunyarbrough.laptracks.ui.viewmodels

import com.shaunyarbrough.laptracks.service.AccountService
import com.shaunyarbrough.laptracks.ui.views.LoginDestination
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import com.shaunyarbrough.laptracks.ui.views.SignupDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val accountService: AccountService
) : LapTrackViewModel() {

	val isError = MutableStateFlow(false)
	fun onSignUpClick(
		openAndPopUp: (String, String) -> Unit,
		email: String, password: String,
		passwordConfirmation: String
	) {
		launchCatching {
			//TODO: Figure out a better way to handle auth error. This works for now
			if (password != passwordConfirmation){
				throw Exception("Passwords do not match")
			}
				accountService.signUp(email,password)
			if (accountService.hasUser()) {
				openAndPopUp(ParticipantDestination.route, SignupDestination.route)
			}
		}
	}

	fun onSignInClick(openAndPopUp: (String, String) -> Unit){
		openAndPopUp(LoginDestination.route,SignupDestination.route)
	}
}