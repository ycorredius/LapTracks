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
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptracks.R
import com.example.laptracks.convertLongToString
import com.example.laptracks.data.Student
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.viewmodels.WorkoutViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

object ParticipantSummaryDestination : NavigationDestination{
  override val route = "participant_summary"
  override val titleRes = R.string.practice_summary
}

@Composable
fun PracticeSummaryBody(
  participants: List<Student>
){
}
@Composable
fun PracticeSummaryScreen(
  workoutViewModel: WorkoutViewModel,
  onCancelButtonClick: () -> Unit
){
  //Intervals onparticipanClick participantTimes onfinishclick oncanclebuttonclick
  val workoutUiState by workoutViewModel.workoutUiState.collectAsState()
  val studentsUiState by workoutViewModel.studentsUiState.collectAsState()

  val date = SimpleDateFormat("dd-MM-yyy")
  val currentDate = date.format(Date())

  var totalTime by remember { mutableLongStateOf(0L) }
  var isTimerRunning by remember { mutableStateOf(false)}
  var isEnabled by remember { mutableStateOf(isTimerRunning)}

  LaunchedEffect(key1 = isTimerRunning, key2 = totalTime){
    if(isTimerRunning){
      delay(100)
      totalTime +=100L
    }
  }

  Column (
    modifier = Modifier
      .fillMaxHeight()
      .fillMaxWidth(),
    verticalArrangement = Arrangement.SpaceBetween
  ){
    Column(
      modifier = Modifier
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "$currentDate - Practice ",
          textAlign = TextAlign.Center,
          fontSize = 22.sp
        )
        Text(text = "${workoutUiState.interval} meters", fontSize = 22.sp)
      }
      Spacer(modifier = Modifier.padding(10.dp))
      Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ){
        workoutUiState.participantsList.forEach {
            participant ->
          Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp, 10.dp)
          ) {
            Button(
              onClick = { workoutViewModel.setParticipantTime(participant.displayName, totalTime) },
              enabled = isEnabled) {
              Text(participant.displayName, fontSize = 18.sp)
            }
            LazyRow (modifier = Modifier.weight(1f)){
              workoutUiState.participantTimes.getValue(participant.displayName).forEachIndexed {
                index, time ->
                item {
                  Column (modifier = Modifier.padding(5.dp,0.dp)){
                    val lap = (index+1).toString()
                    Text(text = "Lap $lap", fontSize = 16.sp)
                    Text(text = convertLongToString(time), modifier = Modifier.padding(5.dp, 0.dp), fontSize = 14.sp)
                  }
                }
              }
            }
          }
        }
      }
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
      Text(text = convertLongToString(totalTime), fontSize = 30.sp)
      Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ){
        Button(onClick = { onCancelButtonClick() }) {
          Text(text = "Cancel")
        }
        if(isTimerRunning) {
          Button(onClick = {
            isTimerRunning = !isTimerRunning
            isEnabled = !isEnabled
          }
          ) {
            Text(text = "Pause")
          }
        } else {
          Button(onClick = {
            isTimerRunning = !isTimerRunning
            isEnabled = !isEnabled}
          ) {
            Text(text = "Start")
          }
        }
        Button(onClick = { /*onFinishClick()*/ }, enabled = !isEnabled) {
          Text(text = "Finish")
        }
      }
    }
  }
}

@Preview
@Composable
fun PracticeSummaryScreenPreview(){
//  PracticeSummaryScreen(
//
//  )
}