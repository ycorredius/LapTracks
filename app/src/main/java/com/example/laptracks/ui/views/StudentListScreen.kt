package com.example.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laptracks.LapTrackAppTopAppBar
import com.example.laptracks.R
import com.example.laptracks.data.Student
import com.example.laptracks.data.Workout
import com.example.laptracks.ui.AppViewModelProvider
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.viewmodels.StudentListViewModel
import com.example.laptracks.ui.viewmodels.StudentUiModel

object StudentListDestination : NavigationDestination {
  override val route = "studentList"
  override val titleRes = R.string.student_list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
  viewModel: StudentListViewModel = viewModel(factory = AppViewModelProvider.Factory),
  navigateToStudentDetails: (Int) -> Unit,
  navigateUp: () -> Unit,
  navigateToStudentEntry: () -> Unit
) {
  val studentListUiState by viewModel.studentListUiState.collectAsState()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(StudentListDestination.titleRes),
        canNavigateBack = true,
        scrollBehavior = scrollBehavior,
        navigateUp = navigateUp
      )
    },
  ) { innerPadding ->
    StudentListBody(
      students = studentListUiState.studentList,
      modifier = Modifier.padding(innerPadding),
      navigateToStudentDetails,
      navigateToStudentEntry
    )


  }
}

@Composable
fun StudentListBody(
  students: List<StudentUiModel>,
  modifier: Modifier = Modifier,
  navigateToStudentDetails: (Int) -> Unit,
  navigateToStudentEntry: () -> Unit
) {
  Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
    if (students.isNotEmpty()) {
      LazyColumn(modifier) {
        items(students) { student ->
          StudentListItem(
            student,
            modifier = Modifier
              .padding(8.dp)
              .clickable { navigateToStudentDetails(student.student.id) }
          )
        }
      }
    } else {
      Text(text = "No Students available")
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
      Button(onClick = { navigateToStudentEntry() }) {
        Text(text = "Add Student")
      }
    }
  }
}


@Composable
fun StudentListItem(
  student: StudentUiModel,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()
    ) {
      Text(
        text = " ${student.student.firstName} ${student.student.lastName}",
        style = MaterialTheme.typography.titleLarge
      )
      Spacer(modifier = Modifier.weight(1f))
      Text(
        text = "${student.workouts.size} Workouts",
        style = MaterialTheme.typography.titleMedium
      )
    }
  }
}

@Composable
@Preview
fun StudentListBodyPreview() {
  StudentListBody(
    students = listOf(StudentUiModel(student = Student(0,"bily","smith","bsmith"), workouts = listOf(
      Workout(0, "somedate", emptyList(),"400",0)
    ))),
    navigateToStudentDetails = {},
    navigateToStudentEntry = {}
  )
}

@Composable
@Preview
fun EmptyStudentListBodyPreview() {
  StudentListBody(
    students = listOf(),
    navigateToStudentDetails = {},
    navigateToStudentEntry = {}
  )
}