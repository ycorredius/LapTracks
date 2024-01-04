package com.example.laptracks.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PracticeSummaryScreen(
  participants: List<String>,
  interval: String,
  onParticipantClick: (String,String) -> Unit,
  participantTimes: Map<String,List<String>>
){
  val date = SimpleDateFormat("dd-MM-yyy ' - Practice'")
  val currentDate = date.format(Date())

  var totalTime by remember { mutableStateOf(0L)}
  var isTimerRunning by remember { mutableStateOf(false)}
  var isEnabled by remember { mutableStateOf(isTimerRunning)}
  var min = "%02d".format((totalTime/1000)/60)
  var sec = "%02d".format((totalTime/1000)%60)

  LaunchedEffect(key1 = isTimerRunning, key2 = totalTime){
    if(isTimerRunning){
      delay(100)
      totalTime +=100L
      min = "%02d".format((totalTime/1000)/60)
      sec = "%02d".format((totalTime/1000)%60)
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
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Text(
          text = currentDate,
          textAlign = TextAlign.Center
        )
        Text(text = "$interval meters")
      }
      Spacer(modifier = Modifier.padding(10.dp))
      Column {
        participants.forEach {
            participant ->
          Row {
            Button(
              onClick = { onParticipantClick(participant, "$min:$sec") },
              enabled = isEnabled) {
              Text(participant)
            }
            Row {
              participantTimes.getValue(participant).forEach{
                time ->
                  Text(text = time)
              }
            }
          }
        }
      }
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
      Text(text = "$min:$sec", fontSize = 16.sp)
      Row (horizontalArrangement = Arrangement.SpaceBetween){
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
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { /*TODO*/ }, enabled = !isEnabled) {
          Text(text = "Finish")
        }
      }
    }
  }
}

@Preview
@Composable
fun PracticeSummaryScreenPreview(){
  PracticeSummaryScreen(
    participants = listOf("Billy", "Jamie", "Moe"),
    onParticipantClick = {participant, time -> },
    interval = "400",
    participantTimes = mapOf("Billy" to listOf("05:30"))
  )
}