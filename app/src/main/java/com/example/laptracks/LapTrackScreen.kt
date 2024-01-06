package com.example.laptracks

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.laptracks.data.DataSource
import com.example.laptracks.ui.screens.IntervalScreen
import com.example.laptracks.ui.screens.ParticipantScreen
import com.example.laptracks.ui.screens.PracticeSummaryScreen
import com.example.laptracks.ui.screens.ResultScreen


enum class LapTrackScreen(@StringRes val title: Int){
  Participants(title = R.string.participants),
  Intervals(title = R.string.intervals),
  PracticeSummary(title = R.string.practice_summary),
  Results(title = R.string.results)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LapTrackTopBar(
  canNavigateBack: Boolean,
  currentScreen: LapTrackScreen,
  navigateUp: () -> Unit
){
  TopAppBar(
    title = { Text(stringResource(currentScreen.title))},
    navigationIcon = {
      if(canNavigateBack) {
        IconButton(onClick = navigateUp) {
          Icon(imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back))
        }
      }
    },
  )
}

@Composable
fun LapTrackApp(viewModel: LapTrackViewModel = viewModel()){
  val navController: NavHostController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentScreen = LapTrackScreen.valueOf(
    backStackEntry?.destination?.route ?: LapTrackScreen.Participants.name
  )
  Scaffold (
    topBar = {
      LapTrackTopBar(
        currentScreen = currentScreen,
        canNavigateBack = navController.previousBackStackEntry != null,
        navigateUp = { navController.navigateUp()}
      )
    },
    modifier = Modifier.padding(20.dp)
  ){
    innerPadding ->
    val uiState by viewModel.uiState.collectAsState()
    NavHost(
      navController = navController,
      startDestination = LapTrackScreen.Participants.name,
      modifier = Modifier.padding(innerPadding)
    ) {

      composable(route = LapTrackScreen.Participants.name){
        ParticipantScreen(
          students = DataSource.students,
          participants = uiState.participants,
          onCheckBoxChange = { viewModel.setParticipants(it) },
          onNextButtonClick = {navController.navigate(LapTrackScreen.Intervals.name)}
          )
      }

      composable(route = LapTrackScreen.Intervals.name){
        IntervalScreen(
          participants = uiState.participants,
          intervals = DataSource.intervals,
          selectedInterval = uiState.interval,
          onIntervalChange = {viewModel.setInterval(it)},
          onNextButtonClick = {navController.navigate(LapTrackScreen.PracticeSummary.name)},
          onCancelButtonClick = {viewModel.onResetClick(navController)}

        )
      }

      composable(route = LapTrackScreen.PracticeSummary.name){
        PracticeSummaryScreen(
          participants = uiState.participants,
          interval = uiState.interval,
          onParticipantClick = { participant , time ->
            viewModel.setParticipantTime(participant,time)
          },
          participantTimes = uiState.participantTimes,
          onFinishClick = {navController.navigate(LapTrackScreen.Results.name)},
          onCancelButtonClick = {viewModel.onResetClick(navController)}

        )
      }

      composable(route = LapTrackScreen.Results.name){
        ResultScreen(
          participants = uiState.participants,
          participantTimes = uiState.participantTimes,
          onCompleteClick = { viewModel.onResetClick(navController) }
        )
      }
    }
  }
}

private fun LapTrackViewModel.onResetClick(
  navController: NavHostController
){
  this.resetWorkout()
  navController.popBackStack(LapTrackScreen.Participants.name, inclusive = false)
}