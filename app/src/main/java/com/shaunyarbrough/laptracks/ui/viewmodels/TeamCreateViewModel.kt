package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.service.TeamService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamCreateViewModel @Inject constructor(
	private val teamService: TeamService
) : LapTrackViewModel() {

	var teamState by mutableStateOf(TeamState())
		private set

	fun updateUiState(teamDetails: TeamDetails) {
		teamState = TeamState(
			teamDetails = teamDetails,
			isTeamValid = validateTeam(teamDetails.name)
		)
	}

	private fun validateTeam(name: String): Boolean {
		return name.isNotBlank()
	}

	suspend fun saveTeam() {
		if (teamState.isTeamValid) {
			teamService.createTeam(teamState.teamDetails.toTeam())
		}
	}
}

private fun TeamDetails.toTeam(): Team = Team(
	name = name
)

data class TeamDetails(
	val name: String = "",
	val userId: String = ""
)


data class TeamState(
	val teamDetails: TeamDetails = TeamDetails(),
	val isTeamValid: Boolean = false
)