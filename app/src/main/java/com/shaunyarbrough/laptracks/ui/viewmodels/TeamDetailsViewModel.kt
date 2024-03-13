package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.service.TeamService
import com.shaunyarbrough.laptracks.ui.views.TeamsDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val teamService: TeamService
) : LapTrackViewModel() {
	var teamDetailsUiState: TeamDetailsUiState by mutableStateOf(TeamDetailsUiState.Loading)
		private set

	private val teamId: String =
		checkNotNull(savedStateHandle[TeamsDetailsDestination.teamIdArgs])

	init {
		fetchTeamDetails()
	}

	private fun fetchTeamDetails() {
		viewModelScope.launch {
			teamDetailsUiState = TeamDetailsUiState.Loading
			teamDetailsUiState = try {
				TeamDetailsUiState.TeamDetailsSuccess(
					teamService.getTeam(teamId).toTeamWithStudentsDetails()
				)
			} catch (e: Exception) {
				TeamDetailsUiState.Error
			}
		}
	}
}

sealed interface TeamDetailsUiState {
	data class TeamDetailsSuccess(val teamDetailsWithStudentsDetails: TeamWithStudentsDetails) :
		TeamDetailsUiState

	data object Error : TeamDetailsUiState

	data object Loading : TeamDetailsUiState
}

data class TeamWithStudentsDetails(
	val name: String = "",
	val students: List<Student?>
)
