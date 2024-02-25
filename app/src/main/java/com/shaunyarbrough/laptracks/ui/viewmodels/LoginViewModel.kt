package com.shaunyarbrough.laptracks.ui.viewmodels

import com.shaunyarbrough.laptracks.service.AccountService
import com.shaunyarbrough.laptracks.ui.views.LoginDestination
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import com.shaunyarbrough.laptracks.ui.views.SignupDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val accountService: AccountService
): LapTrackViewModel() {

	fun onSignInClick(openAndPopUp: (String,String) -> Unit, email: String, password: String){
		launchCatching {
			accountService.signIn(email,password)
			if (accountService.hasUser()) {
				openAndPopUp(ParticipantDestination.route, LoginDestination.route)
			}
		}
	}

	fun onSignUpClick(openAndPopUp: (String, String) -> Unit){
		openAndPopUp(SignupDestination.route,LoginDestination.route)
	}

	fun onContinueAsGuest(openAndPopUp: (String, String) -> Unit){
		launchCatching {
			accountService.anonymousSignIn()
			openAndPopUp(ParticipantDestination.route, LoginDestination.route)
		}
	}
}