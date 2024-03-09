package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shaunyarbrough.laptracks.ServiceLocator.studentWorkoutRepository
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.service.AccountService
import com.shaunyarbrough.laptracks.service.StudentService
import com.shaunyarbrough.laptracks.service.TeamService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentEntryViewModel @Inject constructor(
	private val studentService: StudentService,
	teamService: TeamService
	) :
	ViewModel() {

	var studentUiState by mutableStateOf(StudentUiState())
		private set

	val teams = teamService.teams

	fun updateUiState(studentDetails: StudentDetails) {
		studentUiState = StudentUiState(
			studentDetails = studentDetails,
			isStudentValid = validateStudent(studentDetails)
		)
	}

	private fun validateStudent(uiState: StudentDetails = studentUiState.studentDetails): Boolean {
		return with(uiState) {
			firstName.isNotBlank() && lastName.isNotBlank() && displayName.isNotBlank() && teamId.isNotBlank()
		}
	}

	suspend fun saveStudent() {
		if (validateStudent()) {
			studentService.createStudent(studentUiState.studentDetails.toStudent())
		}
	}
}

data class StudentUiState(
	val studentDetails: StudentDetails = StudentDetails(),
	val isStudentValid: Boolean = false,
)

data class StudentDetails(
	val id: String = "",
	val firstName: String = "",
	val lastName: String = "",
	val displayName: String = "",
	val workouts: Int = 0,
	val teamId: String = ""
)

fun StudentDetails.toStudent(): Student = Student(
	id = id,
	firstName = firstName,
	lastName = lastName,
	displayName = displayName,
	teamId = teamId
)

fun Student.toStudentUiState(isStudentValid: Boolean = false): StudentUiState = StudentUiState(
  studentDetails = this.toStudentDetails(),
  isStudentValid =  isStudentValid
)

fun Student.toStudentDetails(): StudentDetails = StudentDetails(
	id = id,
	firstName = firstName,
	lastName = lastName,
	displayName = displayName
)

fun StudentDetails.isValid(): Boolean {
	return firstName.isNotEmpty() && lastName.isNotEmpty() && displayName.isNotEmpty()
}