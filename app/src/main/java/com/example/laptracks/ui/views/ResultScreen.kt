package com.example.laptracks.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptracks.R
import com.example.laptracks.ui.components.FormattedTimeStamp
import com.example.laptracks.formatResults
import java.text.SimpleDateFormat
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResultScreen(
  participantTimes: Map<String, List<Long>>,
  onCompleteClick: (String, String) -> Unit,
  onRestClick: () -> Unit,
) {

  val date = SimpleDateFormat("dd-MM-yyy")
  val currentDate = date.format(Date())

  Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
    Column {
      for ((key, value) in participantTimes) {
        Row(
          modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
        ) {
          Text(text = key, fontSize = 20.sp, modifier = Modifier.weight(0.2f))
          LazyRow(modifier = Modifier.weight(0.5f)) {
            value.forEachIndexed { index, time ->
              item {
                Column(modifier = Modifier.padding(5.dp, 0.dp)) {
                  val lap = (index + 1).toString()
                  Text(text = "Lap $lap", fontSize = 16.sp)
                  FormattedTimeStamp(time = time, fontSize = 14.sp)
                }
              }
            }
          }
        }
      }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
      Button(
        onClick = { onCompleteClick(currentDate.toString(), formatResults(participantTimes)) },
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(stringResource(R.string.complete))
      }
      OutlinedButton(onClick = { onRestClick() }, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.reset))
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ResultScreenPreview() {
  ResultScreen(
    participantTimes = mapOf(
      "Billy" to listOf(100000L, 245000L),
      "Jamie" to listOf(),
      "Moe" to listOf()
    ),
    onCompleteClick = { _: String, _: String -> },
    onRestClick = {}
  )
}