package com.shaunyarbrough.laptracks.data

import com.google.firebase.firestore.DocumentId

data class Student(
    @DocumentId val id: String = "",
    val firstName: String ="",
    val lastName: String ="",
    val displayName: String = "",
    val teamId: String =""
)
