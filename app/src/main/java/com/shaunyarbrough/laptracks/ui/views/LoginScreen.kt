package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.viewmodels.AuthViewModel

object LoginDestination : NavigationDestination {
	override val route = "login"
	override val titleRes = R.string.login
}

@Composable
fun LoginScreen(
	navController: NavController,
	authViewModel: AuthViewModel = hiltViewModel<AuthViewModel>()
) {
	val auth = authViewModel.authUiState.auth
	var hasError by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		//todo: Add error messages
		Text(
			text = stringResource(id = R.string.welcome),
			style = MaterialTheme.typography.titleMedium,
			fontSize = 25.sp
		)
		Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
			OutlinedTextField(
				value = auth.email,
				onValueChange = { authViewModel.updateUiState(auth.copy(email = it)) },
				label = {
					Text(
						stringResource(id = R.string.email)
					)
				})
			OutlinedTextField(
				value = auth.password,
				onValueChange = { authViewModel.updateUiState(auth.copy(password = it)) },
				label = {
					Text(
						stringResource(
							id = R.string.password
						)
					)
				})
		}
		Button(
			onClick = {
				authViewModel.login(navController)
				if (authViewModel.authUiState.hasError) {
					hasError = authViewModel.authUiState.hasError
				}
			},
			modifier = Modifier.fillMaxWidth(.72f),
			shape = MaterialTheme.shapes.extraSmall,
		) {
			Text(stringResource(id = R.string.login))
		}

	}
}

@Preview
@Composable
fun LoginScreenPreview() {
	LoginScreen(navController = rememberNavController())
}