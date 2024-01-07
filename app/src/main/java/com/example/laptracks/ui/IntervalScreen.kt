package com.example.laptracks.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laptracks.R

@Composable
fun IntervalScreen(
  participants: List<String>,
  intervals: List<String>,
  selectedInterval: String,
  onIntervalChange: (String) -> Unit = {},
  onNextButtonClick: () -> Unit = {},
  onCancelButtonClick: () -> Unit = {}
){
  var expanded by remember { mutableStateOf(false)}
  val context = LocalContext.current
  var selectedText by remember { mutableStateOf(selectedInterval)}
  Column(
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier.fillMaxHeight()
  ) {
    Row {
      Column(modifier = Modifier.weight(1f)) {
        Text(stringResource(R.string.participants), fontSize = 20.sp)
        Divider(modifier = Modifier.width(dimensionResource(R.dimen.interval_divider)).padding(0.dp, 5.dp, 0.dp, 25.dp))
        participants.forEach{
          participant ->
          Row {
            Text(text = participant, fontSize = 18.sp)
          }
        }
      }
      Column(modifier = Modifier.weight(1f).height(IntrinsicSize.Min)) {
        Text(stringResource(R.string.distance), fontSize = 20.sp)
        Divider(modifier = Modifier.width(dimensionResource(R.dimen.interval_divider)).padding(0.dp, 5.dp, 0.dp, 25.dp))
        Box {
          OutlinedTextField(
            value = selectedText,
            label = { Text(text = "Select Interval") },
            onValueChange = { selectedText = it },
            trailingIcon = {
              val icon =
                if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
              Icon(icon, contentDescription = "Some Icon")
            },
            readOnly = true
          )
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight()
              .padding(top = 8.dp)
              .clip(MaterialTheme.shapes.extraSmall)
              .clickable { expanded = true },
            color = Color.Transparent,
          ) {}
        }
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
      OutlinedButton(onClick = { onCancelButtonClick() }) {
        Text(stringResource(R.string.cancel))
      }
      Button(onClick = { onNextButtonClick() }, enabled = selectedText != context.getString(R.string.intervals)) {
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
    intervals = listOf("1","2","3","4"),
    selectedInterval = "Intervals"
  )
}