package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.service.TeamService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
	private val teamService: TeamService
) : LapTrackViewModel() {

	var teamUiState: TeamUiState by mutableStateOf(TeamUiState.Loading)
		private set

	init {
		getTeams()
	}

	fun getTeams() {
		viewModelScope.launch {
			teamUiState = TeamUiState.Loading
			teamUiState = try {
				TeamUiState.Success(teamService.getTeams())
			} catch (e: Exception) {
				TeamUiState.Error
			}
		}
	}
}

sealed interface TeamUiState {
	data class Success(val teams: List<Team>) : TeamUiState
	data object Loading : TeamUiState
	data object Error : TeamUiState
}