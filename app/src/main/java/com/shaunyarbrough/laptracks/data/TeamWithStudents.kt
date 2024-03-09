package com.shaunyarbrough.laptracks.data

import com.google.firebase.firestore.DocumentId
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamWithStudentsDetails

data class TeamWithStudents(
    @DocumentId val id: String ="",
    val name: String ="",
    val students: List<Student?> = emptyList()
){
    fun toTeamWithStudentsDetails(): TeamWithStudentsDetails = TeamWithStudentsDetails(
        name = name, students = students
    )
}
