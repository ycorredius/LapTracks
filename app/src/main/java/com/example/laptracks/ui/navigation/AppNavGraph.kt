package com.example.laptracks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptracks.ui.AppViewModelProvider
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
){

  NavHost(
    navController = navController,
    startDestination = ParticipantDestination.route,
    modifier = modifier
  ){
    composable(route = ParticipantDestination.route){
      ParticipantScreen(
        navigateToStudentEntry = {navController.navigate(StudentEntryDestination.route)},
        navigateToInterval = {navController.navigate(IntervalDestination.route)},
        workoutViewModel = viewModel
      )
    }

    composable(route = StudentEntryDestination.route){
      StudentEntryScreen(
        navigateUp = { navController.navigateUp() },
        navigateBack = { navController.popBackStack() }
      )
    }

    composable(route = IntervalDestination.route){
      IntervalScreen(
        navigateToParticipantSummary = {navController.navigate(ParticipantSummaryDestination.route)},
        viewModel
      )
    }

    composable(route = ParticipantSummaryDestination.route){
      PracticeSummaryScreen(
        workoutViewModel = viewModel,
        onCancelButtonClick = { viewModel.resetWorkout() }
      )
    }
  }
}

private fun WorkoutViewModel.onResetClick(
  navController: NavHostController
){
  this.resetWorkout()
  navController.navigate(ParticipantDestination.route)
}