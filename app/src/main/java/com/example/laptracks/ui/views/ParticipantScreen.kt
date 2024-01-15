package com.example.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    ) { innerPadding ->

    ParticipantBody(
      modifier = Modifier.padding(innerPadding),
      participants = workoutUiState.participantsList,
      students = studentUiState.studentsList,
      onCheckBoxChange = { workoutViewModel.setParticipants(it) },
      navigateToInterval = navigateToInterval,
      navigateToStudentEntry = navigateToStudentEntry
    )

  }
}

@Composable
private fun ParticipantBody(
  modifier: Modifier,
  participants: Map<String,List<Long>>,
  students: List<Student>,
  navigateToInterval: () -> Unit,
  onCheckBoxChange: (Student) -> Unit = {},
  navigateToStudentEntry: () -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    if (students.isEmpty()){
      Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Currently no students saved!",
          modifier = Modifier,
          textAlign = TextAlign.Center,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold)
      }
    }else {
      ParticipantList(
        studentList = students,
        participants = participants,
        onCheckBoxChange = { onCheckBoxChange(it) }
      )
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      FloatingActionButton(
        onClick = { navigateToStudentEntry() },
        shape = CircleShape,
        modifier = Modifier
          .padding(16.dp)
          .align(alignment = Alignment.End)
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add button"
        )
      }
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
  participants: Map<String,List<Long>>,
  onCheckBoxChange: (Student) -> Unit
){
  Column {
    studentList.forEach { student ->
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Checkbox(
          checked = participants.containsKey(student.displayName),
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
    participants = mapOf("BSmith" to listOf(1_000L, 2_000L)),
    students = listOf(
      Student(firstName = "Billy", lastName = "Smith", displayName = "BSmith")
    ),
    onCheckBoxChange = { /* nothing */},
    navigateToInterval = { /* nothing */},
    navigateToStudentEntry = {}
  )
}

@Preview
@Composable
fun ParticipantEmptyScreenPreview(){
  ParticipantBody(
    modifier = Modifier,
    participants = emptyMap(),
    students = emptyList(),
    onCheckBoxChange = { /* nothing */},
    navigateToInterval = { /* nothing */},
    navigateToStudentEntry = {}
  )
}