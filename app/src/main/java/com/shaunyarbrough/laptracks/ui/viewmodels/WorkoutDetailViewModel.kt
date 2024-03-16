package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.service.WorkoutService
import com.shaunyarbrough.laptracks.ui.views.WorkoutDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	workoutService: WorkoutService
) : ViewModel() {

	private val workoutId: String =
		checkNotNull(savedStateHandle[WorkoutDetailsDestination.workoutIdArgs])

	var uiState: WorkoutDetailsUiState = WorkoutDetailsUiState()
	init {
		viewModelScope.launch {
			val workoutStudent = workoutService.getWorkout(workoutId)
			uiState = WorkoutDetailsUiState(
				studentDetails = workoutStudent.keys.first()?.toStudentDetails() ?: StudentDetails(),
				workoutDetails = workoutStudent.values.first()?.toWorkoutDetails() ?: WorkoutDetails()
			)
		}
	}


}

private fun Workout.toWorkoutDetails() = WorkoutDetails(
	date = date,
	lapList = lapList,
	interval = interval,
	totalTime = totalTime
)

data class WorkoutDetails(
	val date: String = "",
	val lapList: List<Long> = emptyList(),
	val interval: String = "",
	val totalTime: Long = 0L,
	val studentId: String = ""
)

data class WorkoutDetailsUiState(
	val workoutDetails: WorkoutDetails = WorkoutDetails(),
	val studentDetails: StudentDetails = StudentDetails()
)