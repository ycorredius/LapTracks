package com.example.laptracks.ui

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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PracticeSummaryScreen(
  participants: List<String>,
  interval: String,
  onParticipantClick: (String,String) -> Unit,
  participantTimes: Map<String,List<String>>,
  onFinishClick: () -> Unit = {},
  onCancelButtonClick: () -> Unit = {}
){

  //TODO: update according to the warning
  val date = SimpleDateFormat("dd-MM-yyy")
  val currentDate = date.format(Date())

  var totalTime by remember { mutableLongStateOf(0L) }
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
      Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ){
        participants.forEach {
            participant ->
          Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(0.dp, 10.dp)
          ) {
            Button(
              onClick = { onParticipantClick(participant, "$min:$sec") },
              enabled = isEnabled) {
              Text(participant, fontSize = 18.sp)
            }
            LazyRow (modifier = Modifier.weight(1f)){
              participantTimes.getValue(participant).forEachIndexed {
                index, time ->
                item {
                  Column (modifier = Modifier.padding(5.dp,0.dp)){
                    val lap = (index+1).toString()
                    Text(text = "Lap $lap", fontSize = 16.sp)
                    Text(text = time, modifier = Modifier.padding(5.dp, 0.dp), fontSize = 14.sp)
                  }
                }
              }
            }
          }
        }
      }
    }
    Column (horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
      Text(text = "$min:$sec", fontSize = 30.sp)
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
        Button(onClick = { onFinishClick() }, enabled = !isEnabled) {
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
    onParticipantClick = { participant, time -> mapOf("Billy" to listOf("0:00"))},
    interval = "400",
    participantTimes = mapOf("Billy" to listOf("0:00"), "Jamie" to listOf(), "Moe" to listOf())
  )
}