package com.shaunyarbrough.laptracks.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.data.WorkoutRepository
import com.shaunyarbrough.laptracks.ui.views.WorkoutDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	workoutRepository: WorkoutRepository
) : ViewModel() {

	private val workoutId: Int =
		checkNotNull(savedStateHandle[WorkoutDetailsDestination.workoutIdArgs])

	val workoutUiState: StateFlow<WorkoutDetailsUiState> =
		workoutRepository.getWorkoutWithStudent(workoutId)
			.filterNotNull()
			.map {
				Log.d("WorkoutDetailsViewModel", "")
				WorkoutDetailsUiState(
					workoutDetails = it.workout.toWorkoutDetails(),
					studentDetails = it.student.toStudentDetails()
				)
			}
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(3_000L),
				initialValue = WorkoutDetailsUiState()
			)
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
)

data class WorkoutDetailsUiState(
	val workoutDetails: WorkoutDetails = WorkoutDetails(),
	val studentDetails: StudentDetails = StudentDetails()
)