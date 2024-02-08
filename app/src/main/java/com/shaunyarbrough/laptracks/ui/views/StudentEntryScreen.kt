package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.AppViewModelProvider
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetails
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentEntryViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentUiState
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
          if (viewModel.studentUiState.isStudentValid) navigateBack()
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
  var isError by rememberSaveable { mutableStateOf(false) }

  Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
    StudentEntryForm(
      onChangeValue = onChangeValue,
      studentDetails = studentUiState.studentDetails,
      isError = isError
    )
    Button(onClick = {
      onSaveClick()
      isError = !studentUiState.isStudentValid
                     }, modifier = Modifier.fillMaxWidth()) {
      Text(stringResource(R.string.save))
    }
  }
}

@Composable
fun StudentEntryForm(
  onChangeValue: (StudentDetails) -> Unit = {},
  studentDetails: StudentDetails,
  isError: Boolean
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
      isError = isError && studentDetails.firstName.isEmpty(),
      supportingText = {
                       if(studentDetails.firstName.isEmpty() && isError){
                         Text(text = "First Name Can't be Blank", color = MaterialTheme.colorScheme.error)
                       }
      },
      modifier = Modifier.fillMaxWidth(),
      enabled = true,
      singleLine = true
    )
    OutlinedTextField(
      value = studentDetails.lastName,
      onValueChange = { onChangeValue(studentDetails.copy(lastName = it)) },
      label = { Text(stringResource(R.string.last_name)) },
      isError = isError && studentDetails.lastName.isEmpty(),
      supportingText = {
        if(studentDetails.lastName.isEmpty() && isError){
          Text(text = "Last Name Can't be Blank", color = MaterialTheme.colorScheme.error)
        }
      },
      modifier = Modifier.fillMaxWidth(),
      enabled = true,
      singleLine = true
    )
    OutlinedTextField(
      value = studentDetails.displayName,
      onValueChange = { onChangeValue(studentDetails.copy(displayName = it)) },
      label = { Text(stringResource(R.string.display_name)) },
      isError = studentDetails.displayName.isEmpty() && isError,
      supportingText = {
        if(studentDetails.displayName.isEmpty() && isError){
          Text(text = "Display Name Can't be Blank", color = MaterialTheme.colorScheme.error)
        }
      },
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
