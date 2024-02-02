package com.example.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laptracks.LapTrackAppTopAppBar
import com.example.laptracks.R
import com.example.laptracks.ui.AppViewModelProvider
import com.example.laptracks.ui.navigation.NavigationDestination
import com.example.laptracks.ui.theme.LapTracksTheme
import com.example.laptracks.ui.viewmodels.StudentDetails
import com.example.laptracks.ui.viewmodels.StudentEntryViewModel
import com.example.laptracks.ui.viewmodels.StudentUiState
import kotlinx.coroutines.launch

object StudentEntryDestination : NavigationDestination {
  override val route = "studentEntry"
  override val titleRes = R.string.student_entry
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentEntryScreen(
  navigateBack: () -> Unit,
  navigateUp: () -> Unit,
  viewModel: StudentEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
  val coroutine = rememberCoroutineScope()
  Scaffold(
    topBar = {
      LapTrackAppTopAppBar(
        title = stringResource(StudentEntryDestination.titleRes),
        canNavigateBack = true,
        navigateUp = navigateUp
      )
    }
  ) { innerPadding ->

    StudentEntryBody(
      studentUiState = viewModel.studentUiState,
      onChangeValue = viewModel::updateUiState,
      onSaveClick = {
        coroutine.launch {
          viewModel.saveStudent()
          navigateBack()
        } },
      modifier = Modifier
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
    )
  }
}

@Composable
fun StudentEntryBody(
  modifier: Modifier = Modifier,
  onChangeValue: (StudentDetails) -> Unit,
  onSaveClick: () -> Unit,
  studentUiState: StudentUiState
) {
  Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
    StudentEntryForm(
      onChangeValue = onChangeValue,
      studentDetails = studentUiState.studentDetails
    )
    Button(onClick = { onSaveClick() }, modifier = Modifier.fillMaxWidth()) {
      Text(stringResource(R.string.save))
    }
  }
}

@Composable
fun StudentEntryForm(
  onChangeValue: (StudentDetails) -> Unit = {},
  studentDetails: StudentDetails
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = Modifier
      .fillMaxHeight()
  ) {
    OutlinedTextField(
      value = studentDetails.firstName,
      onValueChange = { onChangeValue(studentDetails.copy(firstName = it)) },
      label = { Text(stringResource(R.string.first_name)) },
      modifier = Modifier.fillMaxWidth(),
      enabled = true,
      singleLine = true
    )
    OutlinedTextField(
      value = studentDetails.lastName,
      onValueChange = { onChangeValue(studentDetails.copy(lastName = it)) },
      label = { Text(stringResource(R.string.last_name)) },
      modifier = Modifier.fillMaxWidth(),
      enabled = true,
      singleLine = true
    )
    OutlinedTextField(
      value = studentDetails.displayName,
      onValueChange = { onChangeValue(studentDetails.copy(displayName = it)) },
      label = { Text(stringResource(R.string.display_name)) },
      modifier = Modifier.fillMaxWidth(),
      enabled = true,
      singleLine = true
    )
  }
}

@Preview
@Composable
fun StudentEntryPreview() {
  LapTracksTheme {
    StudentEntryBody(
      studentUiState = StudentUiState(
        StudentDetails(
          firstName = "Bill", lastName = "Smith", displayName = "BSmith"
        )
      ),
      modifier = Modifier,
      onChangeValue = { /* nothing */ },
      onSaveClick = { /* nothing */ }
    )
  }
}
