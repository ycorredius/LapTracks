package com.shaunyarbrough.laptracks.data

import com.google.firebase.firestore.DocumentId

data class Team(
    @DocumentId val id: String ="",
    val name: String = "",
    val userId: String = ""
)