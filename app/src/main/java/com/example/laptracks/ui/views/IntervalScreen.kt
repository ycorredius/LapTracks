package com.example.laptracks.ui.views

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laptracks.LapTrackAppTopAppBar
import com.example.laptracks.R
import com.example.laptracks.data.DataSource
import com.example.laptracks.data.Student
import com.example.laptracks.ui.AppViewModelProvider
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.theme.LapTracksTheme
import com.example.laptracks.ui.viewmodels.WorkoutViewModel

object IntervalDestination : NavigationDestination {
  override val route = "intervals"
  override val titleRes = R.string.intervals
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalScreen(
  viewModel: WorkoutViewModel = hiltViewModel()
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val workoutUiState by viewModel.workoutUiState.collectAsState()
  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(IntervalDestination.titleRes),
        canNavigateBack = false,
        scrollBehavior = scrollBehavior
      )
    }
  ) { innerPadding ->

    IntervalBody(
      participants = workoutUiState.participantsList,
      selectedInterval = workoutUiState.interval,
      setInterval = { viewModel.setInterval(it) },
      modifier = Modifier.padding(innerPadding)
    )
  }
}

@Composable
private fun IntervalBody(
  participants: List<Student>,
  selectedInterval: String,
  setInterval: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val intervals = DataSource.intervals
  var expanded by remember { mutableStateOf(false) }
  var selectedText by remember { mutableStateOf(selectedInterval) }

  Column(
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .fillMaxHeight()
      .padding(16.dp)
  ) {
    Row(modifier) {
      Column(modifier = Modifier.weight(1f)) {
        Text(stringResource(R.string.participants), fontSize = 20.sp)
        Divider(
          modifier = Modifier
            .width(dimensionResource(R.dimen.interval_divider))
            .padding(0.dp, 5.dp, 0.dp, 25.dp)
        )
        Column {
          participants.forEach { participant ->
            Text(text = participant.displayName, fontSize = 18.sp)
          }
        }

      }
      Column(
        modifier = Modifier
          .weight(1f)
          .height(IntrinsicSize.Min)
      ) {
        Text(stringResource(R.string.distance), fontSize = 20.sp)
        Divider(
          modifier = Modifier
            .width(dimensionResource(R.dimen.interval_divider))
            .padding(0.dp, 5.dp, 0.dp, 25.dp)
        )
        Box {
          OutlinedTextField(
            value = selectedText,
            label = { Text(text = "Select Interval", fontSize = 14.sp) },
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
              .clip(MaterialTheme.shapes.extraSmall)
              .clickable { expanded = true },
            color = Color.Transparent,
          ) {}
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
          intervals.forEach { interval ->
            DropdownMenuItem(text = { Text(text = interval) },
              onClick = {
                selectedText = interval
                expanded = !expanded
                setInterval(interval)
              }
            )
          }
        }
      }
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Button(
        onClick = { /*onNextButtonClick()*/ },
        enabled = selectedText != context.getString(R.string.intervals),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(stringResource(R.string.next))
      }
      OutlinedButton(onClick = { /*onCancelButtonClick()*/ }, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.cancel))
      }
    }
  }
}

@Preview
@Composable
fun IntervalScreenPreview() {
  LapTracksTheme {
    IntervalBody(
      participants = listOf(Student(firstName = "Bill", lastName = "smith", displayName = "BSmith")),
      selectedInterval = "800",
      setInterval = {}
    )
  }
}