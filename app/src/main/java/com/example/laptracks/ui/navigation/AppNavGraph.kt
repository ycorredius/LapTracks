package com.example.laptracks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptracks.ui.viewmodels.WorkoutViewModel
import com.example.laptracks.ui.views.IntervalDestination
import com.example.laptracks.ui.views.IntervalScreen
import com.example.laptracks.ui.views.ParticipantDestination
import com.example.laptracks.ui.views.ParticipantScreen
import com.example.laptracks.ui.views.StudentEntryDestination
import com.example.laptracks.ui.views.StudentEntryScreen

@Composable
fun AppNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier
){
  NavHost(
    navController = navController,
    startDestination = ParticipantDestination.route,
    modifier = modifier
  ){
    composable(route = ParticipantDestination.route){
      ParticipantScreen(
        navigateToStudentEntry = {navController.navigate(StudentEntryDestination.route)},
        navigateToInterval = {navController.navigate(IntervalDestination.route)}
      )
    }

    composable(route = StudentEntryDestination.route){
      StudentEntryScreen(
        navigateUp = { navController.navigateUp() },
        navigateBack = { navController.popBackStack() }
      )
    }

    composable(route = IntervalDestination.route){
      IntervalScreen()
    }
  }
}