package com.shaunyarbrough.laptracks.service

import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.data.TeamWithStudents
import kotlinx.coroutines.flow.Flow

interface TeamService {
    val teams: Flow<List<Team>>
    suspend fun getTeam(id: String): TeamWithStudents
    suspend fun createTeam(team: Team)
    suspend fun updateTeam(team: Team)
}