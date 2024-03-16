package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.data.WorkoutRoom
import com.shaunyarbrough.laptracks.service.StudentService
import com.shaunyarbrough.laptracks.service.TeamService
import com.shaunyarbrough.laptracks.service.WorkoutService
import com.shaunyarbrough.laptracks.ui.views.ParticipantDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
	private val studentService: StudentService,
	private val teamService: TeamService,
	private val workoutService: WorkoutService
) : LapTrackViewModel() {

	private val _uiState = MutableStateFlow(WorkoutUiState())
	val workoutUiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

	var teamUiState: TeamUiState by mutableStateOf(TeamUiState.Loading)
		private set

	var studentsUiState: StudentsUiState by mutableStateOf(StudentsUiState.Loading)
		private set

	init {
		getTeams()
	}

	private fun getTeams() {
		viewModelScope.launch {
			teamUiState = TeamUiState.Loading
			teamUiState = try {
				TeamUiState.Success(teamService.getTeams())
			} catch (e: Exception) {
				TeamUiState.Error
			}
		}
	}

	fun getStudents(teamId: String): StudentsUiState {
		launchCatching {
			studentsUiState = StudentsUiState.Loading
			studentsUiState = try {
				StudentsUiState.Success(studentService.getStudents(teamId))
			} catch (e: Exception) {
				StudentsUiState.Error
			}
		}
		return studentsUiState
	}

	suspend fun updateStudent(teamId: String): List<Student?> {
		return studentService.getStudents(teamId)
	}

	fun setParticipants(participant: Student) {
		_uiState.update { currentState ->
			val newParticipants =
				if (currentState.participantsList.containsKey(participant)) {
					currentState.participantsList.filter { it.key != participant }
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
				participant ->
				val workout = Workout(
					date = SimpleDateFormat("MM-dd-yyyy").format(Date()),
					interval = workoutUiState.value.interval,
					lapList = participant.value,
					studentId = participant.key.id,
					totalTime = workoutUiState.value.totalTime
				)
				workoutService.createWorkout(workout)
			}
		}
	}

	fun updateTotalTime(totalTime: Long) {
		_uiState.update {
			it.copy(totalTime = totalTime)
		}
	}

	fun onCancelClick(
		navController: NavHostController
	) {
		resetWorkout()
		navController.navigate(ParticipantDestination.route)
	}
}

//TODO: Reconfigure this to create an more readable and optimized experience
fun workoutDetailsToWorkout(
	studentId: Int,
	laps: List<Long>,
	date: String,
	interval: String,
	totalTime: Long
): WorkoutRoom {
	return WorkoutRoom(
		id = 0,
		date = date,
		studentId = studentId,
		lapList = laps,
		interval = interval,
		totalTime = totalTime
	)
}

data class WorkoutUiState(
	val participantsList: Map<Student, List<Long>> = mapOf(),
	val interval: String = "",
	val date: String = SimpleDateFormat("MM-dd-yyyy").format(Date()),
	val totalTime: Long = 0L,
)

sealed interface StudentsUiState {
	data class Success(val students: List<Student?>) : StudentsUiState
	data object Loading : StudentsUiState
	data object Error : StudentsUiState
}