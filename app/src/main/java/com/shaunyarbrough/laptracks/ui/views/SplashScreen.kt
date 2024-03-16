package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.viewmodels.SplashScreenViewModel
import kotlinx.coroutines.delay

object SplashScreenDestination: NavigationDestination{
	override val route = "splash_screen"
	override val titleRes = R.string.splash_screen
}
@Composable
fun SplashScreen(
	openAndPopUp: (String,String) -> Unit,
	viewModel: SplashScreenViewModel = hiltViewModel()
){
	Image(
		painterResource(id = R.drawable.background_clone),
		contentDescription = "background image",
		contentScale = ContentScale.Crop,
	)
	LaunchedEffect(true){
		delay(3_000L)
		viewModel.onAppStart(openAndPopUp)
	}
}