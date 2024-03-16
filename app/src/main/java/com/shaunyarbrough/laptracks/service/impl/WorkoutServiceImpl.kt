package com.shaunyarbrough.laptracks.service.impl

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.service.WorkoutService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkoutServiceImpl @Inject constructor() : WorkoutService {
	override suspend fun getWorkout(id: String): Map<Student?, Workout?> {
		val workoutRef = Firebase.firestore
			.collection(WORKOUT_COLLECTION)
			.document(id)
			.get()
			.await()
			.toObject<Workout>()

		val studentRef = workoutRef?.studentId?.let {
			Firebase.firestore.collection(STUDENT_COLLECTION)
				.document(it)
				.get().await().toObject<Student>()
		}

		return mapOf(studentRef to workoutRef)
	}

	override suspend fun createWorkout(workout: Workout) {
		try {
			val studentRef = Firebase.firestore
				.collection(STUDENT_COLLECTION)
				.document(workout.studentId)

			if (!studentRef.get().await().contains(WORKOUT_COLLECTION)) {
				studentRef.collection(WORKOUT_COLLECTION).document().set(hashMapOf<String, Any>())
			}

			studentRef.collection(WORKOUT_COLLECTION).add(workout).await()
		} catch (e: Exception) {
			Log.e("WorkoutCreation,", "Something went wrong $e")
		}
	}

	override suspend fun deleteWorkout(workoutId: String) {
		TODO("Not yet implemented")
	}

	companion object {
		private const val WORKOUT_COLLECTION = "workouts"
		private const val WORKOUT_ID = "id"
		private const val STUDENT_COLLECTION = "students"

	}
}