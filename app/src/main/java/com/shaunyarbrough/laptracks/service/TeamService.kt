package com.shaunyarbrough.laptracks.service

import com.shaunyarbrough.laptracks.data.Team
import kotlinx.coroutines.flow.Flow

interface TeamService {
    val teams: Flow<List<Team>>
    suspend fun getTeam(id: String): Team?
    suspend fun createTeam(team: Team)
    suspend fun updateTeam(team: Team)
}