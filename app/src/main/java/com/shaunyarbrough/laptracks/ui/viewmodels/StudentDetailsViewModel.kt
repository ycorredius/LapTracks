package com.shaunyarbrough.laptracks.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.StudentWithWorkouts
import com.shaunyarbrough.laptracks.service.StudentService
import com.shaunyarbrough.laptracks.ui.views.StudentDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentDetailsViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val studentService: StudentService
) : ViewModel() {

	private val studentId: String =
		checkNotNull(savedStateHandle[StudentDetailsDestination.studentIdArg])

	private val _uiState: MutableStateFlow<StudentDetailsUiState> = MutableStateFlow<StudentDetailsUiState>(StudentDetailsUiState.Loading)
	val uiState: StateFlow<StudentDetailsUiState> = _uiState
	

	init {
		viewModelScope.launch {
			getStudent()
		}
	}

	suspend fun removeUser(studentId: String) {
		studentService.deleteStudent(studentId)
	}

	private suspend fun getStudent() {
		_uiState.value = StudentDetailsUiState.Loading
		 try {
			studentService.getStudentWithWorkouts(studentId).collect{
				studentWithWorkout ->
				_uiState.value = StudentDetailsUiState.Success(studentWithWorkout)
			}
		} catch (e: Exception){
			Log.e("StudentDetailsError", "Something went wrong: $e")
			_uiState.value = StudentDetailsUiState.Error
		}
	}
}

sealed interface StudentDetailsUiState {
	data class Success(val studentDetails: StudentWithWorkouts) :
		StudentDetailsUiState

	data object Error: StudentDetailsUiState

	data object Loading: StudentDetailsUiState
}

