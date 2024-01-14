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
  onCompleteClick: (String, String) -> Unit
) {

  val workoutUiState by viewModel.workoutUiState.collectAsState()
  val date = SimpleDateFormat("dd-MM-yyy")
  var currentDate = date.format(Date())
  var workout = ""
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  workoutUiState.participantsList.forEach { participant ->
    val result = participant.key + participant.value.map{ convertLongToString(it)}.joinToString(", ") + "\n"
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
      onCompleteClick = { currentDate, workout ->
        onCompleteClick(currentDate,workout)
      },
      modifier = Modifier.padding(innerPadding)
    )
  }
}

@Composable
private fun ResultBody(
  participants: Map<String, List<Long>>,
  currentDate: String,
  workout: String,
  onCompleteClick: (String,String) -> Unit,
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
          Text(text = participant.key, modifier = Modifier.weight(0.2f), fontSize = 20.sp)
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
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
      Button(
        onClick = { onCompleteClick(currentDate, workout) },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(stringResource(R.string.complete))
      }
    }
  }
}

@Preview
@Composable
fun ResultScreenPreview() {
  ResultBody(
    participants = mapOf("Bill" to listOf(5_000L)),
    onCompleteClick = { _, _ ->},
    currentDate = "1234",
    workout = "TEsting"
  )
}