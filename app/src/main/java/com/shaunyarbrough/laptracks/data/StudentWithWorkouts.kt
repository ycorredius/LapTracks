package com.shaunyarbrough.laptracks.data

import com.google.firebase.firestore.DocumentId

data class StudentWithWorkouts(
	@DocumentId val id: String = "",
	val firstName: String = "",
	val lastName: String = "",
	val displayName: String = "",
	val teamId: String = "",
	val workouts: List<Workout?>
)