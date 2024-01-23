package com.example.laptracks.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptracks.LapTrackAppTopAppBar
import com.example.laptracks.R
import com.example.laptracks.convertLongToString
import com.example.laptracks.data.Student
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.viewmodels.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.Date

object ResultScreenDestination : NavigationDestination {
  override val titleRes = R.string.results
  override val route = "results"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
  viewModel: WorkoutViewModel,
  navigateUp: () -> Unit,
  onSendEmailClick: (String,String) -> Unit,
  onResetClick: () -> Unit
) {

  val workoutUiState by viewModel.workoutUiState.collectAsState()
  val date = SimpleDateFormat("dd-MM-yyyy")
  val currentDate = date.format(Date())
  var workout = ""
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  workoutUiState.participantsList.forEach { participant ->
    val result = "${participant.key.displayName} - " + participant.value.map{ convertLongToString(it)}.joinToString(", ") + "\n"
    workout += result
  }
  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(ResultScreenDestination.titleRes),
        canNavigateBack = true,
        scrollBehavior = scrollBehavior,
        navigateUp = navigateUp
      )
    }
  ) { innerPadding ->
    ResultBody(
      participants = workoutUiState.participantsList,
      currentDate = currentDate.toString(),
      workout,
      onSendEmailClick = { currentDate, workout ->
        onSendEmailClick(currentDate,workout)
      },
      modifier = Modifier.padding(innerPadding),
      onResetClick = onResetClick,
      onSaveClick = { viewModel.saveWorkout() }
    )
  }
}

@Composable
private fun ResultBody(
  participants: Map<Student, List<Long>>,
  currentDate: String,
  workout: String,
  onSendEmailClick: (String,String) -> Unit,
  onResetClick: () -> Unit,
  onSaveClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier) {
      participants.forEach { participant ->
        Row(
          modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = participant.key.displayName, modifier = Modifier.weight(0.2f), fontSize = 20.sp)
          Spacer(modifier = Modifier)
          LazyRow(modifier = Modifier.weight(0.5f)) {
            participants.forEach { participant ->
              itemsIndexed(participant.value) { index, item ->
                Column(modifier = Modifier.padding(5.dp, 0.dp)) {
                  val lap = (index + 1).toString()
                  Text(text = "Lap $lap", fontSize = 16.sp)
                  Text(text = convertLongToString(item), fontSize = 14.sp)
                }
              }
            }
          }
        }
      }
    }
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
      Button(onClick = {
        onSaveClick() }, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.save))
      }
      Button(
        onClick = { onSendEmailClick(currentDate, workout) },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(stringResource(R.string.send_email))
      }
      Button(
        onClick = { onResetClick() },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(stringResource(R.string.reset))
      }
    }
  }
}

@Preview
@Composable
fun ResultScreenPreview() {
  ResultBody(
    participants = mapOf(Student(id = 0, firstName = "Bily", lastName = "Smith", displayName = "BSmith") to listOf(5_000L)),
    onSendEmailClick = { _, _ ->},
    currentDate = "1234",
    workout = "Testing",
    onResetClick = {},
    onSaveClick = {}
  )
}