package com.shaunyarbrough.laptracks.ui.viewmodels

import com.shaunyarbrough.laptracks.service.TeamService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamsViewModel @Inject constructor(
    teamService: TeamService
): LapTrackViewModel(){
    val teams = teamService.teams
}