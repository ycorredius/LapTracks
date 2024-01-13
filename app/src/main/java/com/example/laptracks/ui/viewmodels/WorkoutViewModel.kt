package com.example.laptracks.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptracks.data.Student
import com.example.laptracks.data.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val studentRepository: StudentRepository) : ViewModel() {
  companion object {
//    private const val PARTICIPANTS_KEY = "participants_key"
    private const val TIMEOUT_MILLIS = 5_000L
  }

  private val _uiState = MutableStateFlow(WorkoutUiState())
  val workoutUiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

  val studentsUiState: StateFlow<StudentsUiState> =
    studentRepository.getStudentsStream().map { StudentsUiState(it) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = StudentsUiState()
      )

//  init {
//    savedStateHandle.get<List<Student>>(PARTICIPANTS_KEY)?.let { participantsList ->
//      setParticipantsList(participantsList)
//    }
//  }
//
//  private fun setParticipantsList(participantsList: List<Student>) {
//    // Save participantsList to SavedStateHandle
//    savedStateHandle.set(PARTICIPANTS_KEY, participantsList)
//
//    // Update the UI state
//    _uiState.update { currentState ->
//      currentState.copy(participantsList = participantsList)
//    }
//  }

  //TODO: Add ability to add and remove student from workout participant times map
  // A better idea is to possibly implement a the participants times map rather than a list.
    fun setParticipants(participant: Student) {
    _uiState.update { currentState ->
      val newParticipants = if (currentState.participantsList.contains(participant)) {
        currentState.participantsList.filter { it != participant }
      } else {
        currentState.participantsList + listOf(participant)
      }
      currentState.copy(participantsList = newParticipants)
    }
//    savedStateHandle[PARTICIPANTS_KEY] = _uiState.value.participantsList
  }

  fun setInterval(interval: String){
    _uiState.update {
      currentState ->
      currentState.copy(interval = interval)
    }
  }

  fun setParticipantTime(participant: String, timeStamp: Long){
    _uiState.update {
        currentState ->
      val newMap = currentState.participantTimes.keys.associateWith {
          key ->
        currentState.participantTimes.getValue(key) + if (key == participant) {
          listOf(timeStamp)
        } else {
          emptyList()
        }
      }
      currentState.copy(participantTimes = newMap)
    }
  }

  fun resetWorkout(){
    _uiState.value = WorkoutUiState()
  }
}

data class WorkoutUiState(
  val participantsList: List<Student> = listOf(),
  val interval: String = "",
  val participantTimes: Map<String, List<Long>> = mapOf()
)

data class StudentsUiState(
  val studentsList: List<Student> = listOf()
)