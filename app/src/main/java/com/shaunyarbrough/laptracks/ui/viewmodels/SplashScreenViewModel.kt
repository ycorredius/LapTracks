package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.shaunyarbrough.laptracks.service.AccountService
import com.shaunyarbrough.laptracks.ui.views.LoginDestination
import com.shaunyarbrough.laptracks.ui.views.SplashScreenDestination
import com.shaunyarbrough.laptracks.ui.views.TeamsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
	private val accountService: AccountService
): ViewModel() {

	fun onAppStart(openAndPopUp: (String,String) -> Unit){
		if(accountService.hasUser()){
			openAndPopUp(TeamsDestination.route,SplashScreenDestination.route)
		} else {
			openAndPopUp(LoginDestination.route,SplashScreenDestination.route)
		}
	}
}