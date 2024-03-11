package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.service.StudentService
import com.shaunyarbrough.laptracks.ui.views.StudentDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDetailsViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val studentService: StudentService
) : ViewModel() {

	private val studentId: String =
		checkNotNull(savedStateHandle[StudentDetailsDestination.studentIdArg])

	var uiState: StudentDetailsUiState = StudentDetailsUiState()

	init {
		viewModelScope.launch {
			val student = studentService.getStudent(studentId)
			if (student != null) {
				uiState = StudentDetailsUiState(
					studentDetails = student.toStudentDetails(),
				)
			}
		}
	}

	suspend fun removeUser() {
		studentService.deleteStudent(uiState.studentDetails.id)
	}
}


data class StudentDetailsUiState(
	val studentDetails: StudentDetails = StudentDetails(),
	val workoutDetails: List<Workout>? = emptyList()
)