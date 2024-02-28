package com.shaunyarbrough.laptracks.data

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithStudent(
	@Embedded val student: Student,
	@Relation(
		parentColumn = "id",
		entityColumn = "studentId"
	)
	val workout: Workout
)
