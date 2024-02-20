package com.shaunyarbrough.laptracks.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.StudentRepository
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.ui.views.StudentDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StudentDetailsViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val studentRepository: StudentRepository
) : ViewModel() {
  private val studentId: Int =
    checkNotNull(savedStateHandle[StudentDetailsDestination.studentIdArg])

  val studentDetailsUiState: StateFlow<StudentDetailsUiState> =
    studentRepository.loadStudentWithWorkouts(studentId)
      .filterNotNull()
      .map {
        try {
          val value = it.values.first()
          val workoutDetails = if(value.isNullOrEmpty()) value else null
          StudentDetailsUiState(
            studentDetails = it.keys.first().toStudentDetails(),
            workoutDetails = workoutDetails
          )
        } catch (e: NoSuchElementException){
          Log.d("StudentDetailsViewModel", "$it")
          StudentDetailsUiState()
        }
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StudentDetailsUiState()
      )

  suspend fun removeUser(){
    studentRepository.deleteStudent(studentDetailsUiState.value.studentDetails.toStudent())
  }
}



data class StudentDetailsUiState(
  val studentDetails: StudentDetails = StudentDetails(),
  val workoutDetails: List<Workout>? = emptyList()
)