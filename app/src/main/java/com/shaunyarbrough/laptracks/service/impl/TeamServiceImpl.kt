package com.shaunyarbrough.laptracks.service.impl

import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.service.AccountService
import com.shaunyarbrough.laptracks.service.TeamService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TeamServiceImpl @Inject constructor(
    private val auth: AccountService
) : TeamService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val teams: Flow<List<Team>>
        get() = auth.currentUser.flatMapLatest { teams ->
            Firebase.firestore
                .collection(TEAM_COLLECTION)
                .whereEqualTo(USER_ID, teams?.id)
                .dataObjects()
        }

    override suspend fun getTeam(id: String): Team? {
        return Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(id).get().await().toObject()
    }

    override suspend fun createTeam(team: Team) {
        val teamWithUserId = team.copy(userId = auth.currentUserId)
        Firebase.firestore.collection(TEAM_COLLECTION)
            .add(teamWithUserId).await()
    }

    override suspend fun updateTeam(team: Team) {
        Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(team.id).set(team).await()
    }

    companion object {
        private const val TEAM_COLLECTION = "teams"
        private const val USER_ID = "userId"
    }
}