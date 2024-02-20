package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.ui.views.StudentEditDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentEditViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val studentWorkoutRepository: StudentWorkoutRepository
) : ViewModel() {

	private val studentId: Int = checkNotNull(savedStateHandle[StudentEditDestination.studentIdArg])

	var studentUiState by mutableStateOf(StudentUiState())
		private set

	init{
		viewModelScope.launch {
			studentUiState =  studentWorkoutRepository.getStudent(studentId)
				.filterNotNull()
				.first()
				.toStudentUiState()
		}
	}


	fun updateUiState(studentDetails: StudentDetails) {
		studentUiState = StudentUiState(
			studentDetails = studentDetails,
			isStudentValid = validateStudent(studentDetails)
		)
	}

	suspend fun updateStudent(){
		if(studentUiState.studentDetails.isValid()){
			studentWorkoutRepository.updateStudent(studentUiState.studentDetails.toStudent())
		}
	}

	private fun validateStudent(studentDetails: StudentDetails = studentUiState.studentDetails): Boolean {
		return with(studentDetails){
			firstName.isNotBlank() && lastName.isNotBlank() && displayName.isNotBlank()
		}
	}
}