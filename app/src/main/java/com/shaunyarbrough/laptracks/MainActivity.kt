package com.shaunyarbrough.laptracks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shaunyarbrough.laptracks.ui.navigation.AppNavHost
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // If I add this the app breaks
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			LapTracksTheme {
				val appState = rememberAppState()

				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Scaffold(
						bottomBar = {
							if(appState.shouldBottomBarShow) BottomAppBar(
								containerColor = MaterialTheme.colorScheme.primaryContainer,
								contentPadding = PaddingValues(horizontal = 20.dp),
								modifier = Modifier
									.height(55.dp)
							) {
								BottomBarRow(navController = appState.navController)
							}
						}
					) {
						AppNavHost(
							navController = appState.navController,
							padding = it
						)
					}
				}
			}
		}
	}
}
