package com.example.laptracks.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptracks.data.StudentRepository
import com.example.laptracks.data.Workout
import com.example.laptracks.ui.views.StudentDetailsDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StudentDetailsViewModel(
  savedStateHandle: SavedStateHandle,
  studentRepository: StudentRepository
) : ViewModel() {
  private val studentId: Int =
    checkNotNull(savedStateHandle[StudentDetailsDestination.studentIdArg])

  val studentDetailsUiState: StateFlow<StudentDetailsUiState> =
    studentRepository.loadStudentWithWorkouts(studentId)
      .filterNotNull()
      .map {
        if(it.values.isNotEmpty()) {
          StudentDetailsUiState(
            studentDetails = it.keys.first().toStudentDetails(),
            workoutDetails = it.values.first()
          )
        }else{
          StudentDetailsUiState(
            studentDetails = it.keys.first().toStudentDetails()
          )
        }
      }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StudentDetailsUiState()
      )
}

data class StudentDetailsUiState(
  val studentDetails: StudentDetails = StudentDetails(),
  val workoutDetails: List<Workout>? = emptyList()
)