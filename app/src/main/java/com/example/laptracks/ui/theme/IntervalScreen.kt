package com.example.laptracks.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.laptracks.R

@Composable
fun IntervalScreen(
  participants: List<String>,
  intervals: List<String>,
  onIntervalChange: (String) -> Unit = {},
  onNextButtonClick: () -> Unit = {}
){
  var expanded by remember { mutableStateOf(false)}
  var selectedText by remember { mutableStateOf("Interval")}

  Column(
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier.fillMaxHeight()
  ) {
    Row {
      Column(modifier = Modifier.weight(1f)) {
        Text(stringResource(R.string.participants))
        Divider(modifier = Modifier.width(dimensionResource(R.dimen.interval_divider)))
        participants.forEach{
          participant ->
          Row {
            Text(text = participant)
          }
        }
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(stringResource(R.string.distance))
        Divider(modifier = Modifier.width(dimensionResource(R.dimen.interval_divider)))
        OutlinedTextField(
          value = selectedText,
          label = { Text(text = "Select Interval")},
          onValueChange = {selectedText = it},
          trailingIcon = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Some Icon",
            Modifier.clickable { expanded = !expanded })}
        )
        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
          intervals.forEach{
            interval ->
            DropdownMenuItem(text = { Text(text = interval) },
              onClick = {
                selectedText = interval
                expanded = !expanded
                onIntervalChange(interval)
              }
            )
          }
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
fun IntervalScreenPreview(){
  IntervalScreen(
    participants = listOf("Bill","Bob", "Jamie"),
    intervals = listOf("1","2","3","4")
  )
}