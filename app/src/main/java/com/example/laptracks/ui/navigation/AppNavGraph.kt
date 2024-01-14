package com.example.laptracks.ui.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptracks.R
import com.example.laptracks.ui.AppViewModelProvider
import com.example.laptracks.ui.ResultScreen
import com.example.laptracks.ui.ResultScreenDestination
import com.example.laptracks.ui.viewmodels.WorkoutViewModel
import com.example.laptracks.ui.views.IntervalDestination
import com.example.laptracks.ui.views.IntervalScreen
import com.example.laptracks.ui.views.ParticipantDestination
import com.example.laptracks.ui.views.ParticipantScreen
import com.example.laptracks.ui.views.ParticipantSummaryDestination
import com.example.laptracks.ui.views.PracticeSummaryScreen
import com.example.laptracks.ui.views.StudentEntryDestination
import com.example.laptracks.ui.views.StudentEntryScreen

@Composable
fun AppNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier,
  viewModel: WorkoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
  NavHost(
    navController = navController,
    startDestination = ParticipantDestination.route,
    modifier = modifier
  ) {
    composable(route = ParticipantDestination.route) {
      val workoutViewModel = hiltViewModel<WorkoutViewModel>()
      ParticipantScreen(
        navigateToStudentEntry = { navController.navigate(StudentEntryDestination.route) },
        navigateToInterval = { navController.navigate(IntervalDestination.route) },
        workoutViewModel = workoutViewModel
      )
    }

    composable(route = StudentEntryDestination.route) {
      StudentEntryScreen(
        navigateUp = { navController.navigateUp() },
        navigateBack = { navController.popBackStack() },
      )
    }

    composable(route = IntervalDestination.route) {
      val parentEntry = remember(it) {
        navController.getBackStackEntry(ParticipantDestination.route)
      }
      val parentViewModel = hiltViewModel<WorkoutViewModel>(parentEntry)
      IntervalScreen(
        navigateToParticipantSummary = { navController.navigate(ParticipantSummaryDestination.route) },
        navigateUp = { navController.navigateUp() },
        onCancelClick = { viewModel.onCancelClick(navController) },
        viewModel = parentViewModel
      )
    }

    composable(route = ParticipantSummaryDestination.route) {
      val parentEntry = remember(it) {
        navController.getBackStackEntry(ParticipantDestination.route)
      }
      val parentViewModel = hiltViewModel<WorkoutViewModel>(parentEntry)
      PracticeSummaryScreen(
        workoutViewModel = parentViewModel,
        navigateUp = { navController.navigateUp() },
        onFinishClick = { navController.navigate(ResultScreenDestination.route) },
        onCancelClick = { viewModel.onCancelClick(navController) }
      )
    }
    composable(route = ResultScreenDestination.route) {
      val context = LocalContext.current
      val parentEntry = remember(it) {
        navController.getBackStackEntry(ParticipantDestination.route)
      }
      val parentViewModel = hiltViewModel<WorkoutViewModel>(parentEntry)
      ResultScreen(
        viewModel = parentViewModel,
        navigateUp = { navController.navigateUp() },
        onCompleteClick = { subject: String, workout: String ->
          composeEmail(context, subject = subject, bodyText = workout)
        },
        onResetClick = { viewModel.onCancelClick(navController) }
      )
    }
  }
}

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