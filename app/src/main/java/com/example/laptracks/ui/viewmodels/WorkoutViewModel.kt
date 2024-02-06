package com.example.laptracks.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.laptracks.StudentWorkoutRepository
import com.example.laptracks.data.Student
import com.example.laptracks.data.Workout
import com.example.laptracks.ui.views.ParticipantDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
  private val studentWorkoutRepository: StudentWorkoutRepository,
) : ViewModel() {
  companion object {
    private const val TIMEOUT_MILLIS = 5_000L
  }

  private val _uiState = MutableStateFlow(WorkoutUiState())
  val workoutUiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

  val studentsUiState: StateFlow<StudentsUiState> =
    studentWorkoutRepository.getStudentsStream().map { StudentsUiState(it) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = StudentsUiState()
      )

  fun setParticipants(participant: Student) {
    _uiState.update { currentState ->
      val newParticipants =
        if (currentState.participantsList.containsKey(participant)) {
          currentState.participantsList.filter { it.key != participant}
        } else {
          currentState.participantsList + mapOf(participant to emptyList())
        }
      currentState.copy(participantsList = newParticipants)
    }
  }

  fun setInterval(interval: String) {
    _uiState.update { currentState ->
      currentState.copy(interval = interval)
    }
  }

  fun setParticipantTime(participant: Student, timeStamp: Long) {
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

  fun saveWorkout() {
    viewModelScope.launch(Dispatchers.IO) {
      workoutUiState.value.participantsList.forEach {
        studentWorkoutRepository.insertWorkout(workoutDetailsToWorkout(it.key.id,it.value,workoutUiState.value.date, workoutUiState.value.interval, workoutUiState.value.totalTime))
      }
    }
  }

//  fun launchTime(isTimerRunning: Boolean){
//    viewModelScope.launch {
//      if (isTimerRunning) {
//        delay(100)
//         _uiState.update {
//           it.copy(totalTime = it.totalTime + 100L)
//         }
//      }
//    }
//  }

  fun onCancelClick(
    navController: NavHostController
  ) {
    resetWorkout()
    navController.navigate(ParticipantDestination.route)
  }
}

//TODO: Reconfigure this to create an more readable and optimized experience
fun workoutDetailsToWorkout(studentId: Int, laps: List<Long>, date: String, interval: String, totalTime: Long): Workout{
  return Workout(id = 0,date = date, studentId = studentId, lapList = laps, interval = interval, totalTime = totalTime)
}

data class WorkoutUiState(
  val participantsList: Map<Student, List<Long>> = mapOf(),
  val interval: String = "",
  val date: String = SimpleDateFormat("dd-MM-yyyy").format(Date()),
  val totalTime: Long = 0L
)

data class StudentsUiState(
  val studentsList: List<Student> = listOf()
)

// TODO: This will be used soon when viewing and editing a view.
//data class WorkoutDetails(
//  val date: String = "",
//  val lapList: List<Long> = emptyList(),
//  val studentId: Int = 0
//)