package com.shaunyarbrough.laptracks.ui.views

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.DataSource
import com.shaunyarbrough.laptracks.data.StudentRoom
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutViewModel

object IntervalDestination : NavigationDestination {
  override val route = "intervals"
  override val titleRes = R.string.intervals
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntervalScreen(
  navigateToParticipantSummary: () -> Unit,
  viewModel: WorkoutViewModel,
  navigateUp: () -> Unit,
  onCancelClick: () -> Unit
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val workoutUiState by viewModel.workoutUiState.collectAsState()
  val selectedInterval = workoutUiState.interval.ifBlank { stringResource(R.string.select_intervals) }
  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(IntervalDestination.titleRes),
        canNavigateBack = true,
        scrollBehavior = scrollBehavior,
        navigateUp = navigateUp,
        modifier = Modifier.testTag(stringResource(IntervalDestination.titleRes))
      )
    }
  ) { innerPadding ->
    Box(modifier = Modifier.padding(innerPadding)) {
      IntervalBody(
        participants = workoutUiState.participantsList,
        selectedInterval = selectedInterval,
        setInterval = { viewModel.setInterval(it) },
        navigateToParticipantSummary = navigateToParticipantSummary,
        onCancelClick
      )
    }
  }
}

@Composable
private fun IntervalBody(
  participants: Map<StudentRoom, List<Long>>,
  selectedInterval: String,
  setInterval: (String) -> Unit,
  navigateToParticipantSummary: () -> Unit,
  onCancelClick: () -> Unit
) {
  val context = LocalContext.current
  val intervals = DataSource.intervals
  var expanded by remember { mutableStateOf(false) }
  var selectedText by remember { mutableStateOf(selectedInterval) }

  Column(
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .fillMaxHeight()
  ) {
    Row(modifier = Modifier.padding(15.dp)) {
      Column(modifier = Modifier.weight(1f)) {
        Text(stringResource(R.string.participants), fontSize = 20.sp)
        Divider(
          modifier = Modifier
            .width(dimensionResource(R.dimen.interval_divider))
            .padding(0.dp, 5.dp, 0.dp, 25.dp)
        )
        Column {
          participants.forEach { participant ->
            Text(text = participant.key.displayName, fontSize = 18.sp)
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
        IntervalDropdownMenu(
          onChange = { selectedText = it },
          selectedText = selectedText,
          expanded = expanded,
          onDropdownClick = { expanded = it },
          intervals = intervals,
          setInterval = { setInterval(it) }
        )
      }
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Button(
        onClick = { navigateToParticipantSummary() },
        enabled = selectedText != context.getString(R.string.select_intervals),
        modifier = Modifier
          .fillMaxWidth()
          .testTag(stringResource(id = R.string.next))
      ) {
        Text(stringResource(R.string.next), fontSize = dimensionResource(id = R.dimen.button_font).value.sp)
      }
      OutlinedButton(onClick = { onCancelClick() }, modifier = Modifier
        .fillMaxWidth()
        .testTag(
          stringResource(id = R.string.cancel)
        )) {
        Text(stringResource(R.string.cancel), fontSize = dimensionResource(id = R.dimen.button_font).value.sp)
      }
    }
  }
}

@Composable
private fun IntervalDropdownMenu(
  onChange: (String) -> Unit,
  selectedText: String,
  expanded: Boolean,
  onDropdownClick: (Boolean) -> Unit,
  intervals: List<String>,
  setInterval: (String) -> Unit
) {
  Box {
    OutlinedTextField(
      value = selectedText,
      label = { Text(text = stringResource(R.string.select_intervals), fontSize = 14.sp) },
      onValueChange = { onChange(it) },
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
        .clickable { onDropdownClick(!expanded) }
        .testTag(stringResource(id = R.string.drop_down_button)),
      color = Color.Transparent,
    ) {}
  }
  DropdownMenu(expanded = expanded, onDismissRequest = { onDropdownClick(!expanded) }, modifier = Modifier.testTag(
    stringResource(id = R.string.drop_down_menu) )) {
    intervals.forEach { interval ->
      DropdownMenuItem(text = { Text(text = interval) },
        onClick = {
          onChange(interval)
          onDropdownClick(!expanded)
          setInterval(interval)
        },
        modifier = Modifier.testTag(interval)
      )
    }
  }
}

@Preview
@Composable
fun IntervalScreenPreview() {
  LapTracksTheme {
    IntervalBody(
      participants = mapOf(StudentRoom(id = 0, "Billy","Smith", "BSmith") to listOf(1_000L, 2_000L)),
      selectedInterval = "800",
      setInterval = {},
      navigateToParticipantSummary = {},
      onCancelClick = {}
    )
  }
}