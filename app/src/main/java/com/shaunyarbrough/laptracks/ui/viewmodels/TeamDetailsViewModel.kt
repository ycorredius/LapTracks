package com.shaunyarbrough.laptracks.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.service.TeamService
import com.shaunyarbrough.laptracks.ui.views.TeamsDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val teamService: TeamService
): LapTrackViewModel() {
    val team = MutableStateFlow(TeamWithStudentsDetails("Sometime", emptyList()))

    private val teamId: String =
        checkNotNull(savedStateHandle[TeamsDetailsDestination.teamIdArgs])
    init {
       fetchTeamDetails()
    }

    companion object{
        private val DEFAULT_TEAM = TeamWithStudentsDetails("Sometime", emptyList())
    }

    private fun fetchTeamDetails(){
        viewModelScope.launch {
            try {
                team.value = teamService.getTeam(teamId).toTeamWithStudentsDetails()
            } catch (e: Exception){
                Log.e("TeamDetailsViewModel", "Error fetching team details: $e")
            }
        }
    }
}

data class TeamWithStudentsDetails(
    val name: String = "",
    val students: List<Student?> = emptyList()
)