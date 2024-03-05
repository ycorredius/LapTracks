package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.MutableState
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
): LapTrackViewModel() {

    var teamUiState by mutableStateOf(TeamUiState())
        private set

    fun updateUiState(teamDetails: TeamDetails){
        teamUiState = TeamUiState(
            teamDetails = teamDetails,
            isTeamValid = validateTeam(teamDetails.name)
        )
    }
    private fun validateTeam(name: String): Boolean{
       return name.isNotBlank()
    }

    suspend fun saveTeam(){
        if (teamUiState.isTeamValid){
            teamService.createTeam(teamUiState.teamDetails.toTeam())
        }
    }
}

private fun TeamDetails.toTeam(): Team = Team(
    name = name
)
data class TeamDetails(
    val name: String ="",
    val userId: String = ""
)


data class  TeamUiState(
    val teamDetails: TeamDetails = TeamDetails(),
    val isTeamValid: Boolean = false
)