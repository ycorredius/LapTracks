package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.getLapTimeAverage
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetails
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetailsViewModel

object StudentDetailsDestination : NavigationDestination {
  override val route = "student_details"
  override val titleRes = R.string.student_details
  const val studentIdArg = "studentId"
  val routeWithArg = "$route/{$studentIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailsScreen(
  viewModel: StudentDetailsViewModel = hiltViewModel(),
  navigateUp: () -> Unit
) {
  val studentDetailsUiState by viewModel.studentDetailsUiState.collectAsState()

  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(id = StudentDetailsDestination.titleRes),
        canNavigateBack = true,
        navigateUp = navigateUp
      )
    }
  ) { innerPadding ->

    StudentDetailsBody(
      studentDetailsUiState.studentDetails,
      workouts = studentDetailsUiState.workoutDetails,
      modifier = Modifier.padding(innerPadding)
    )
  }
}

@Composable
fun StudentDetailsBody(
  student: StudentDetails,
  workouts: List<Workout>?,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.padding(20.dp)) {
    Text(text = "${student.firstName} ${student.lastName}", style = MaterialTheme.typography.titleLarge)
    Text(text = student.displayName, style = MaterialTheme.typography.titleMedium)
    Divider(modifier = Modifier.padding(0.dp, 10.dp))
    Column {
      Text(text = "Workouts", style = MaterialTheme.typography.titleMedium)
      if (!workouts.isNullOrEmpty()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
          item {
            Row(modifier = Modifier.padding(10.dp)) {
              TableHeader(text = "Date", weight = 0.3f)
              TableHeader(text = "Lap", weight = 0.2f)
              TableHeader(text = "Average Lap", weight = 0.3f)
            }
          }
          items(workouts) { workout ->
            WorkoutItem(workout)
          }
        }
      } else {
        Text(text = "Sorry no workout data collected.")
      }
    }
  }
}

@Composable
fun WorkoutItem(
  workout: Workout
) {
  Card(shape = MaterialTheme.shapes.extraSmall){
    Row(modifier = Modifier
      .padding(10.dp)
      .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
      TableCell(text = workout.date, weight = 0.3f)
      TableCell(text = "${workout.lapList.size}", weight = 0.2f)
      TableCell(text = getLapTimeAverage(workout.lapList), weight = 0.3f)
    }
  }
}

@Composable
fun RowScope.TableCell(
  text: String,
  weight: Float
) {
  Text(text = text, modifier = Modifier.weight(weight), style = MaterialTheme.typography.bodyMedium)
}

@Composable
fun RowScope.TableHeader(
  text: String,
  weight: Float
) {
  Text(text = text, modifier = Modifier.weight(weight), style = MaterialTheme.typography.bodyLarge)
}

@Preview
@Composable
fun StudentDetailsPreview() {
  LapTracksTheme {
    StudentDetailsBody(
      student = StudentDetails(
        firstName = "billy",
        lastName = "smith",
        displayName = "BSmith"
      ),
      workouts = emptyList()
    )
  }
}