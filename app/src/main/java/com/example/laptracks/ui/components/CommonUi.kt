package com.example.laptracks.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.laptracks.convertLongToString

@Composable
fun FormattedTimeStamp(time: Long, modifier: Modifier = Modifier, fontSize: TextUnit){
  Text(text = convertLongToString(time), modifier = modifier.padding(5.dp, 0.dp), fontSize = fontSize)
}