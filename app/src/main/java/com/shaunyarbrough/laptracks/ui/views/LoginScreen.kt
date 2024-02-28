package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.viewmodels.LoginViewModel

object LoginDestination : NavigationDestination {
	override val route = "login"
	override val titleRes = R.string.login
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
	loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
	openAndPopUp: (String, String) -> Unit
) {
	val controller = LocalSoftwareKeyboardController.current
	var hasError by remember { mutableStateOf(false) }
	var isVisible by remember {
		mutableStateOf(false)
	}
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		//todo: Add error messages
		Text(
			text = stringResource(id = R.string.welcome_back),
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
				},
				singleLine = true,
				enabled = true
			)
			OutlinedTextField(
				value = password,
				onValueChange = { password = it },
				label = {
					Text(stringResource(id = R.string.password))
				},
				enabled = true,
				singleLine = true,
				visualTransformation = if(isVisible) VisualTransformation.None else  PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				trailingIcon = {
					IconButton(onClick = { isVisible = !isVisible }) {
						if (isVisible) {
							Image(
								painter = painterResource(id = R.drawable.eye_closed),
								contentDescription = "Eye closed"
							)
						} else {
							Image(
								painter = painterResource(id = R.drawable.eye_open),
								contentDescription = "Eye open"
							)
						}
					}
				}
			)
		}
		Button(
			onClick = {
				controller?.hide()
				loginViewModel.onSignInClick(openAndPopUp, email, password)
			},
			modifier = Modifier.fillMaxWidth(.72f).padding(0.dp,10.dp),
			shape = MaterialTheme.shapes.extraSmall,
		) {
			Text(stringResource(id = R.string.login))
		}
		Text(
			text = "Create an Account",
			modifier = Modifier.clickable { loginViewModel.onSignUpClick(openAndPopUp) })
	}
}

@Preview
@Composable
fun LoginScreenPreview() {
	LoginScreen(openAndPopUp = { _, _ -> })
}