package com.shaunyarbrough.laptracks.data

import com.google.firebase.firestore.DocumentId

data class StudentWithWorkout(
	@DocumentId val id: String = "",
	val firstName: String = "",
	val lastName: String = "",
	val displayName: String = "",
	val workouts: List<Workout>? = emptyList()
)
