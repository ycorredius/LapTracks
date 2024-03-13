package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.service.StudentService
import com.shaunyarbrough.laptracks.service.TeamService
import com.shaunyarbrough.laptracks.ui.views.StudentEditDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentEditViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val studentService: StudentService,
	private val teamService: TeamService
) : ViewModel() {

	private val studentId: String =
		checkNotNull(savedStateHandle[StudentEditDestination.studentIdArg])

	val teams = teamService.teams

	var uiState by mutableStateOf(StudentUiState())
		private set

	init {
		viewModelScope.launch {
			uiState = studentService.getStudent(studentId)?.toStudentUiState() ?: StudentUiState()
		}
	}


	fun updateUiState(studentDetails: StudentDetails) {
		uiState = StudentUiState(
			studentDetails = studentDetails,
			isStudentValid = validateStudent(studentDetails)
		)
	}

	suspend fun updateStudent() {
		if (uiState.studentDetails.isValid()) {
			studentService.updateStudent(uiState.studentDetails.toStudent())
		}
	}

	private fun validateStudent(studentDetails: StudentDetails = uiState.studentDetails): Boolean {
		return with(studentDetails) {
			firstName.isNotBlank() && lastName.isNotBlank() && displayName.isNotBlank()
		}
	}
}