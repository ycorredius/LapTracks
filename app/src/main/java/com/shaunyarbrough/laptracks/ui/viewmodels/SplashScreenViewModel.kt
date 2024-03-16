package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.shaunyarbrough.laptracks.service.AccountService
import com.shaunyarbrough.laptracks.ui.ScreenRoutes
import com.shaunyarbrough.laptracks.ui.views.SplashScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
	private val accountService: AccountService
): ViewModel() {

	fun onAppStart(openAndPopUp: (String,String) -> Unit){
		if(accountService.hasUser()){
			openAndPopUp(ScreenRoutes.BottomBar.routes,SplashScreenDestination.route)
		} else {
			openAndPopUp(ScreenRoutes.Register.Login.routes,SplashScreenDestination.route)
		}
	}
}