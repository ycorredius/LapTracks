package com.shaunyarbrough.laptracks.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.views.LoginDestination
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import com.shaunyarbrough.laptracks.ui.views.SignupDestination
import com.shaunyarbrough.laptracks.ui.views.SplashScreenDestination
import com.shaunyarbrough.laptracks.ui.views.TeamsDestination
import com.shaunyarbrough.laptracks.ui.views.TeamsDetailsDestination

enum class BottomBarRoutes(
	val id: Int,
	@StringRes val title: Int,
	val routes: String,
	@DrawableRes val icon: Int
) {
	TEAM(2,R.string.teams, TeamsDestination.route, R.drawable.group),
	WORKOUT(1, R.string.participants,ParticipantDestination.route,R.drawable.athlete),
}

sealed class ScreenRoutes(val routes: String){
	data object Splash: ScreenRoutes(SplashScreenDestination.route)
	data object BottomBar: ScreenRoutes("bottomBar"){
		data object Teams: ScreenRoutes(TeamsDestination.route)
		data object TeamsDetails: ScreenRoutes(TeamsDetailsDestination.route)
	}

	data object Register: ScreenRoutes("register"){
		data object Login: ScreenRoutes(LoginDestination.route)
		data object Signup: ScreenRoutes(SignupDestination.route)
	}
}