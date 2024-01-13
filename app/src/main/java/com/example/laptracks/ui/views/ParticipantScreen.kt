package com.example.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laptracks.LapTrackAppTopAppBar
import com.example.laptracks.R
import com.example.laptracks.data.Student
import com.example.laptracks.ui.AppViewModelProvider
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.viewmodels.WorkoutUiState
import com.example.laptracks.ui.viewmodels.WorkoutViewModel

object ParticipantDestination : NavigationDestination {
  override val route = "participants"
  override val titleRes = R.string.participants
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantScreen(
  navigateToStudentEntry: () -> Unit,
  navigateToInterval: () -> Unit,
  workoutViewModel: WorkoutViewModel
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val workoutUiState by workoutViewModel.workoutUiState.collectAsState()
  val studentUiState by workoutViewModel.studentsUiState.collectAsState()
  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(ParticipantDestination.titleRes),
        canNavigateBack = false,
        scrollBehavior = scrollBehavior
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { navigateToStudentEntry() },
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(16.dp)
        ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add button"
        )
      }
    }) { innerPadding ->

    ParticipantBody(
      modifier = Modifier.padding(innerPadding),
      participants = workoutUiState.participantsList,
      students = studentUiState.studentsList,
      onCheckBoxChange = { workoutViewModel.setParticipants(it) },
      navigateToInterval = navigateToInterval
    )

  }
}

@Composable
private fun ParticipantBody(
  modifier: Modifier,
  participants: List<Student>,
  students: List<Student>,
  navigateToInterval: () -> Unit,
  onCheckBoxChange: (Student) -> Unit = {}
) {
  Column(
    modifier = modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    if (students.isEmpty()){
      Text(text = "The are no student currently!")
    }else {
      ParticipantList(
        studentList = students,
        participants = participants,
        onCheckBoxChange = { onCheckBoxChange(it) }
      )
    }
    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.Bottom,
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Button(
        onClick = { navigateToInterval() },
        enabled = participants.isNotEmpty(),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(stringResource(R.string.next), fontSize = 18.sp)
      }
    }
  }
}

@Composable
private fun ParticipantList(
  studentList: List<Student>,
  participants: List<Student>,
  onCheckBoxChange: (Student) -> Unit
){
  Column {
    studentList.forEach { student ->
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Checkbox(
          checked = participants.contains(student),
          onCheckedChange = {
            onCheckBoxChange(student)
          }
        )
        Text(
          text = student.displayName,
          fontSize = 18.sp
        )
      }
    }
  }
}

@Preview
@Composable
fun ParticipantScreenPreview() {
  ParticipantBody(
    modifier = Modifier,
    participants = listOf(
      Student(firstName = "Billy", lastName = "Smith", displayName = "BSmith")
    ),
    students = listOf(
      Student(firstName = "Billy", lastName = "Smith", displayName = "BSmith")
    ),
    onCheckBoxChange = { /* nothing */},
    navigateToInterval = { /* nothing */}
  )
}