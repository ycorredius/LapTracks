package com.example.laptracks.ui.viewmodels

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
  studentRepository: StudentRepository
) : ViewModel() {
  companion object {
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

  fun setParticipants(participant: Student) {
    _uiState.update { currentState ->
      val newParticipants =
        if (currentState.participantsList.containsKey(participant.displayName)) {
          currentState.participantsList.filter { it.key != participant.displayName }
        } else {
          currentState.participantsList + mapOf(participant.displayName to emptyList())
        }
      currentState.copy(participantsList = newParticipants)
    }
  }

  fun setInterval(interval: String) {
    _uiState.update { currentState ->
      currentState.copy(interval = interval)
    }
  }

  fun setParticipantTime(participant: String, timeStamp: Long) {
    _uiState.update { currentState ->
      val newMap = currentState.participantsList.keys.associateWith { key ->
        currentState.participantsList.getValue(key) + if (key == participant) {
          listOf(timeStamp)
        } else {
          emptyList()
        }
      }
      currentState.copy(participantsList = newMap)
    }
  }

  fun resetWorkout() {
    _uiState.value = WorkoutUiState()
  }
}

data class WorkoutUiState(
  val participantsList: Map<String, List<Long>> = mapOf(),
  val interval: String = ""
)

data class StudentsUiState(
  val studentsList: List<Student> = listOf()
)