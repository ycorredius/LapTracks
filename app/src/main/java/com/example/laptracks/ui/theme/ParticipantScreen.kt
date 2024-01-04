package com.example.laptracks.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.laptracks.R

@Composable
fun ParticipantScreen(
  modifier: Modifier = Modifier,
  students: List<String>,
  participants: List<String>,
  onCheckBoxChange: (String) -> Unit = {},
  onNextButtonClick: () -> Unit = {}
){
    Column(
      modifier = modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        students.forEach{
            student ->
          Row {
            Checkbox(
              checked = participants.contains(student),
              onCheckedChange = {
                onCheckBoxChange(student)
              }
            )
            Text(text = student)
          }
        }
      }
      Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)){
        OutlinedButton(onClick = { /*TODO*/ }) {
          Text(stringResource(R.string.cancel))
        }
        Button(onClick = { onNextButtonClick() }) {
          Text(stringResource(R.string.next))
        }
      }
    }
}

@Preview
@Composable
fun ParticipantScreenPreview(){
  ParticipantScreen(
    students = listOf("Billy", "Bob", "Jean"),
    participants = listOf("Billy", "Bob"),
    modifier = Modifier.fillMaxHeight())
}