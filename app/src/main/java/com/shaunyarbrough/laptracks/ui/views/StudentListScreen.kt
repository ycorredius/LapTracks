package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentListViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentUiModel

object StudentListDestination : NavigationDestination {
  override val route = "studentList"
  override val titleRes = R.string.student_list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
  viewModel: StudentListViewModel = hiltViewModel(),
  navigateToStudentDetails: (String) -> Unit,
  navigateUp: () -> Unit,
  navigateToStudentEntry: () -> Unit,
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
    BoxWithConstraints(
      Modifier.padding(innerPadding),
      contentAlignment = Alignment.Center
    ) {
      StudentListBody(
        students = studentListUiState.studentList,
        modifier = Modifier,
        navigateToStudentDetails,
        navigateToStudentEntry
      )
    }
  }
}

@Composable
fun StudentListBody(
  students: List<StudentUiModel>,
  modifier: Modifier = Modifier,
  navigateToStudentDetails: (String) -> Unit,
  navigateToStudentEntry: () -> Unit
) {
  Column(modifier = Modifier
    .fillMaxSize()
    .padding(20.dp),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally) {
    if (students.isNotEmpty()) {
      LazyColumn(modifier) {
        items(students) { student ->
          StudentListItem(
            student,
            modifier = Modifier
              .padding(0.dp, dimensionResource(id = R.dimen.column_padding))
              .clickable { navigateToStudentDetails(student.student.id) }
          )
        }
      }
    } else {
      Text(
        text = stringResource(id = R.string.no_students),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
      )
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
      FloatingActionButton(onClick = { navigateToStudentEntry() }, shape = MaterialTheme.shapes.extraLarge) {
       Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Student")
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
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    shape = MaterialTheme.shapes.extraSmall
  ) {
    Row(
      modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
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
    students = listOf(StudentUiModel(student = Student("best student id","bily","smith","bsmith", teamId = "best team id"), workouts = listOf(
      Workout("best workout id", "somedate", emptyList(),"400","best student id", totalTime = 5_000L)
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