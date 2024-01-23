package com.example.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptracks.LapTrackAppTopAppBar
import com.example.laptracks.R
import com.example.laptracks.convertLongToString
import com.example.laptracks.data.Student
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.theme.LapTracksTheme
import com.example.laptracks.ui.viewmodels.WorkoutViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

object ParticipantSummaryDestination : NavigationDestination {
  override val route = "participant_summary"
  override val titleRes = R.string.practice_summary
}

@Composable
fun PracticeSummaryBody(
  currentDate: String,
  interval: String,
  participants: Map<Student, List<Long>>,
  setParticipantTime: (Student, Long) -> Unit,
  totalTime: Long,
  isEnabled: Boolean,
  isTimerRunning: Boolean,
  onStartClick: () -> Unit,
  modifier: Modifier = Modifier,
  onFinishClick: () -> Unit,
  onCancelClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxHeight()
      .fillMaxWidth(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "$currentDate - Practice ",
          textAlign = TextAlign.Center,
          fontSize = 22.sp
        )
        Text(text = "$interval meters", fontSize = 22.sp)
      }
      Spacer(modifier = Modifier.padding(10.dp))
      ParticipantSummaryLazyColumn(
        participants = participants,
        setParticipantTime = { participant, time ->
          setParticipantTime(participant, time)
        },
        totalTime,
        isEnabled = isEnabled
      )
    }
    Column {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text = convertLongToString(totalTime), fontSize = 30.sp)
        Column(
          modifier = Modifier.fillMaxWidth(),
        ) {
          OutlinedButton(onClick = { onCancelClick() }, modifier = modifier.fillMaxWidth()) {
            Text(text = "Cancel")
          }
          Button(onClick = {
            onStartClick()
          },
            modifier = Modifier.fillMaxWidth()
          ) {
            if (isTimerRunning) Text(text = "Pause") else Text(text = "Start")
          }
          Button(onClick = { onFinishClick() }, enabled = !isEnabled, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Finish")
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeSummaryScreen(
  workoutViewModel: WorkoutViewModel,
  onCancelClick: () -> Unit,
  navigateUp: () -> Unit,
  onFinishClick: () -> Unit
) {
  val workoutUiState by workoutViewModel.workoutUiState.collectAsState()

  val date = SimpleDateFormat("dd-MM-yyy")
  val currentDate = date.format(Date())

  var totalTime by remember { mutableLongStateOf(0L) }
  var isTimerRunning by remember { mutableStateOf(false) }
  var isEnabled by remember { mutableStateOf(isTimerRunning) }
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  LaunchedEffect(key1 = isTimerRunning, key2 = totalTime) {
    if (isTimerRunning) {
      delay(100)
      totalTime += 100L
    }
  }

  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(ParticipantSummaryDestination.titleRes),
        canNavigateBack = true,
        scrollBehavior = scrollBehavior,
        navigateUp = navigateUp
      )
    },
    modifier = Modifier.padding(16.dp)
  ) { innerPadding ->
    PracticeSummaryBody(
      currentDate = currentDate,
      interval = workoutUiState.interval,
      participants = workoutUiState.participantsList,
      totalTime = totalTime,
      isEnabled = isEnabled,
      setParticipantTime = { participant, time ->
        workoutViewModel.setParticipantTime(participant, time)
      },
      isTimerRunning = isTimerRunning,
      onStartClick = {
        isTimerRunning = !isTimerRunning
        isEnabled = !isEnabled
      },
      modifier = Modifier.padding(innerPadding),
      onFinishClick = onFinishClick,
      onCancelClick = onCancelClick
    )
  }
}

@Composable
private fun ParticipantSummaryLazyColumn(
  participants: Map<Student, List<Long>>,
  setParticipantTime: (Student, Long) -> Unit,
  totalTime: Long,
  isEnabled: Boolean
) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    participants.forEach { participant ->
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(0.dp, 10.dp)
      ) {
        Button(
          onClick = { setParticipantTime(participant.key, totalTime) },
          enabled = isEnabled
        ) {
          Text(participant.key.displayName, fontSize = 18.sp)
        }
        LazyRow {
          participant.value.forEachIndexed { index, time ->
            item {
              IntervalLazyColumnItem(index = index, time = time)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun IntervalLazyColumnItem(
  index: Int,
  time: Long
) {
  Column(modifier = Modifier.padding(5.dp, 0.dp)) {
    val lap = (index + 1).toString()
    Text(text = "Lap $lap", fontSize = 16.sp)
    Text(
      text = convertLongToString(time),
      modifier = Modifier.padding(5.dp, 0.dp),
      fontSize = 14.sp
    )
  }
}

@Preview
@Composable
fun PracticeSummaryScreenPreview() {
  LapTracksTheme {
    PracticeSummaryBody(currentDate = "1234",
      interval = "400",
      isTimerRunning = false,
      isEnabled = false,
      onStartClick = {},
      participants = mapOf(Student(id = 0, firstName = "Billy", lastName = "Smith", displayName = "BSmith") to listOf(5_000L)),
      setParticipantTime = {_,_ ->},
      totalTime = 5_000L,
      onFinishClick = {},
      onCancelClick = {}
    )
  }
}