package com.shaunyarbrough.laptracks.service.impl

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.data.TeamWithStudents
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

    override suspend fun getTeam(id: String): TeamWithStudents {
        val teamDoc = Firebase.firestore
            .collection(TEAM_COLLECTION)
            .document(id).get().await()

        val team =
            teamDoc.toObject<TeamWithStudents?>() ?: throw NoSuchElementException("Team not found")

        return team.copy(students = getStudentForTeam(id))
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


    private suspend fun getStudentForTeam(teamId: String): List<Student> {
        val querySnapshot = Firebase.firestore
            .collection(STUDENT_COLLECTION)
            .whereEqualTo(TEAM_ID, teamId)
            .get()
            .await()

        querySnapshot.documents.forEach { documentSnapshot ->
            val rawData = documentSnapshot.data
            Log.d("FirestoreData", "Raw Data: $rawData")
        }

        return querySnapshot.toObjects<Student>()
    }

    companion object {
        private const val TEAM_COLLECTION = "teams"
        private const val TEAM_ID = "teamId"
        private const val STUDENT_COLLECTION = "students"
        private const val USER_ID = "userId"
    }
}