package com.shaunyarbrough.laptracks.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.shaunyarbrough.laptracks.LapTrackAppBottomAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.viewmodels.MainViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutViewModel
import com.shaunyarbrough.laptracks.ui.views.IntervalDestination
import com.shaunyarbrough.laptracks.ui.views.IntervalScreen
import com.shaunyarbrough.laptracks.ui.views.LoginDestination
import com.shaunyarbrough.laptracks.ui.views.LoginScreen
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import com.shaunyarbrough.laptracks.ui.views.ParticipantScreen
import com.shaunyarbrough.laptracks.ui.views.ParticipantSummaryDestination
import com.shaunyarbrough.laptracks.ui.views.PracticeSummaryScreen
import com.shaunyarbrough.laptracks.ui.views.ResultScreen
import com.shaunyarbrough.laptracks.ui.views.ResultScreenDestination
import com.shaunyarbrough.laptracks.ui.views.StudentDetailsDestination
import com.shaunyarbrough.laptracks.ui.views.StudentDetailsScreen
import com.shaunyarbrough.laptracks.ui.views.StudentEntryDestination
import com.shaunyarbrough.laptracks.ui.views.StudentEntryScreen
import com.shaunyarbrough.laptracks.ui.views.StudentListDestination
import com.shaunyarbrough.laptracks.ui.views.StudentListScreen

@Composable
fun AppNavHost(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	mainViewModel: MainViewModel = hiltViewModel(),
	viewModel: WorkoutViewModel = hiltViewModel()
) {
	Scaffold(
		bottomBar = {
			LapTrackAppBottomAppBar(navController)
		}
	) { innerPadding ->
		val currentDestination by mainViewModel.currentDestination.collectAsState()
		val isLoading by mainViewModel.isLoading.collectAsState()
		
		if(isLoading){
			Text(text = "Loading")
		}else {
			NavHost(
				navController = navController,
				startDestination = currentDestination,
				modifier = modifier.padding(innerPadding)
			) {
				composable(route = LoginDestination.route) {
					LoginScreen(navController)
				}
				composable(route = ParticipantDestination.route) {
					ParticipantScreen(
						navigateToInterval = { navController.navigate(IntervalDestination.route) },
						workoutViewModel = viewModel
					)
				}

				composable(route = StudentEntryDestination.route) {
					StudentEntryScreen(
						navigateUp = { navController.navigateUp() },
						navigateBack = { navController.popBackStack() },
					)
				}

				composable(route = IntervalDestination.route) {
					IntervalScreen(
						navigateToParticipantSummary = {
							navController.navigate(
								ParticipantSummaryDestination.route
							)
						},
						navigateUp = { navController.navigateUp() },
						onCancelClick = { viewModel.onCancelClick(navController) },
						viewModel = viewModel
					)
				}

				composable(route = ParticipantSummaryDestination.route) {
					PracticeSummaryScreen(
						navigateUp = { navController.navigateUp() },
						onFinishClick = { navController.navigate(ResultScreenDestination.route) },
						workoutViewModel = viewModel
					)
				}
				composable(route = ResultScreenDestination.route) {
					val context = LocalContext.current

					ResultScreen(
						viewModel = viewModel,
						navigateUp = { navController.navigateUp() },
						onSendEmailClick = { subject: String, workout: String ->
							composeEmail(context, subject = subject, bodyText = workout)
						},
						onResetClick = { viewModel.onCancelClick(navController) }
					)
				}

				composable(route = StudentListDestination.route) {
					StudentListScreen(
						navigateUp = { navController.navigateUp() },
						navigateToStudentDetails = {
							navController.navigate("${StudentDetailsDestination.route}/${it}")
						},
						navigateToStudentEntry = { navController.navigate(StudentEntryDestination.route) },
					)
				}

				composable(
					route = StudentDetailsDestination.routeWithArg,
					arguments = listOf(navArgument(StudentDetailsDestination.studentIdArg) {
						type = NavType.IntType
					})
				) {
					StudentDetailsScreen(navigateUp = { navController.navigateUp() })
				}
			}
		}
	}
}

//Save in case we change our mind on whether or not to use it.
private fun WorkoutViewModel.onCancelClick(
	navController: NavHostController
) {
	this.resetWorkout()
	navController.navigate(ParticipantDestination.route)
}

private fun composeEmail(context: Context, subject: String, bodyText: String) {
	// NOTE: Using intent extras didn't work well for the ACTION_SENDTO, had to use url params instead
	val intent = Intent(Intent.ACTION_SENDTO).apply {
		data = Uri.parse("mailto:?subject=${Uri.encode(subject)}&body=${Uri.encode(bodyText)}")
	}

	try {
		// First show Google's "email app chooser" because it is very difficult for users to reset their
		// default email app - it is not exposed on Android "Default" settings screen - users have to
		// to to Settings -> Apps -> Select app currently set as default email app -> Clear defaults
		context.startActivity(
			Intent.createChooser(
				intent,
				context.getString(R.string.email_chooser_title),
			),
		)
	} catch (e: Exception) {
		// Error opening compose email app.
		print("Compose email app not found.")
	}
}