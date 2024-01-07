package com.example.laptracks.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptracks.R
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ResultScreen(
  participants: List<String>,
  participantTimes: Map<String, List<String>>,
  onCompleteClick: (String,String) -> Unit
) {

  val date = SimpleDateFormat("dd-MM-yyy")
  val currentDate = date.format(Date())
  var workout = ""
  participants.forEach{
     participant ->
      val result = participant + participantTimes.getValue(participant).joinToString(", ") +"\n"
      workout += result
    }
  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
    Column(modifier = Modifier) {
      participants.forEach { participant ->
        Row (modifier = Modifier
          .padding(5.dp)
          .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = participant, modifier = Modifier.weight(0.2f), fontSize = 20.sp)
          Spacer(modifier = Modifier)
          LazyRow (modifier = Modifier.weight(0.5f)){
            participantTimes.getValue(participant).forEachIndexed { index,time ->
              item {
                Column (modifier = Modifier.padding(5.dp,0.dp)){
                  val lap = (index + 1).toString()
                  Text(text = "Lap $lap", fontSize = 16.sp)
                  Text(text = time, fontSize = 14.sp)
                }
              }
            }
          }
        }
      }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
      Button(onClick = { onCompleteClick(currentDate ,workout) }) {
        Text(stringResource(R.string.complete))
      }
    }
  }
}

@Preview
@Composable
fun ResultScreenPreview() {
  ResultScreen(
    participants = listOf("Billy", "Jamie", "Moe"),
    participantTimes = mapOf("Billy" to listOf("05:30"), "Jamie" to listOf(), "Moe" to listOf()),
    onCompleteClick = {subject: String, summary: String ->},
  )
}