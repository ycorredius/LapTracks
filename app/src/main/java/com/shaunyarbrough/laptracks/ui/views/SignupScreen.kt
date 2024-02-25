package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.SignUpViewModel

object SignupDestination : NavigationDestination {
	override val route = "signup"
	override val titleRes = R.string.signup
}

@Composable
fun SignupScreen(
	openAndPopUp: (String, String) -> Unit,
	viewModel: SignUpViewModel = hiltViewModel(),
) {
	val hasError = viewModel.isError.collectAsState()
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var passwordConfirmation by remember { mutableStateOf("") }
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		//todo: Add error messages
		Text(
			text = "Welcome!",
			style = MaterialTheme.typography.titleMedium,
			fontSize = 25.sp
		)
		Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
			OutlinedTextField(
				value = email,
				onValueChange = { email = it },
				label = {
					Text(
						stringResource(id = R.string.email)
					)
				})
			OutlinedTextField(
				value = password,
				onValueChange = { password = it },
				label = {
					Text(stringResource(id = R.string.password))
				},
				visualTransformation = PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				supportingText = {
					if (hasError.value){
						Text(text = "Passwords do not match")
					}
				}
			)
			OutlinedTextField(
				value = passwordConfirmation,
				onValueChange = { passwordConfirmation = it },
				label = {
					Text(text = stringResource(id = R.string.password_confirmation))
				},
				visualTransformation = PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				supportingText = {
					if (hasError.value){
						Text(text = "Passwords do not match")
					}
				}
			)
		}
		Button(
			onClick = {
				viewModel.onSignUpClick(
					openAndPopUp = openAndPopUp,
					email,
					password,
					passwordConfirmation
				)
			},
			modifier = Modifier.fillMaxWidth(.72f),
			shape = MaterialTheme.shapes.extraSmall,
		) {
			Text(stringResource(id = R.string.signup))
		}
		Text(
			text = "Already have an account?",
			modifier = Modifier.clickable { viewModel.onSignInClick(openAndPopUp) })
	}
}

@Preview
@Composable
fun SignupScreenPreview() {
	LapTracksTheme {
		SignupScreen(openAndPopUp = { _, _ -> })
	}
}