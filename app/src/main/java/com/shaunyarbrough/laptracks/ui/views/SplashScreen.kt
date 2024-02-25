package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
		modifier = Modifier
			.size(150.dp)
			.border(BorderStroke(1.dp, Color.Black))
			.background(Color.Yellow)
	)
	LaunchedEffect(true){
		delay(5_000L)
		viewModel.onAppStart(openAndPopUp)
	}
}