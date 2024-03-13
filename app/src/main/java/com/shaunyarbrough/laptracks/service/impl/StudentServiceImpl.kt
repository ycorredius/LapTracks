package com.shaunyarbrough.laptracks.service.impl

import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.shaunyarbrough.laptracks.LapTrackApplication
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.StudentWithWorkouts
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.service.StudentService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudentServiceImpl @Inject constructor() : StudentService {
	override val students: Flow<List<Student>>
		get() = TODO("Not yet implemented")

	override suspend fun getStudent(id: String): Student? {
		return Firebase.firestore
			.collection(STUDENT_COLLECTION)
			.document(id)
			.get().await().toObject<Student>()
	}

	override suspend fun getStudentWithWorkouts(id: String): Flow<StudentWithWorkouts> = flow {
		val studentDoc = Firebase.firestore
			.collection(STUDENT_COLLECTION)
			.document(id).get().await()

		val student =
			studentDoc.toObject<StudentWithWorkouts?>()
				?: throw NoSuchElementException("Student not found")
		val workouts = getWorkouts(id)
		emit(student.copy(workouts = workouts))
	}

	override suspend fun createStudent(student: Student) {
		Firebase.firestore
			.collection(STUDENT_COLLECTION)
			.add(student).addOnSuccessListener {
				Toast.makeText(
					LapTrackApplication.instance,
					"Created successfully",
					Toast.LENGTH_SHORT
				)
					.show()
			}.addOnFailureListener {
				Log.e("Student Creation", "Something went wrong: $it")
			}
	}

	override suspend fun updateStudent(student: Student) {
		try {
			Firebase.firestore
				.collection(STUDENT_COLLECTION)
				.document(student.id).set(student).await()
		} catch (e: Exception) {
			Log.e("UpdatingStudentGoneWrong", "Error: $e")
		}
	}

	override suspend fun deleteStudent(id: String) {
		Firebase.firestore
			.collection(STUDENT_COLLECTION)
			.document(id).delete().await()
	}

	companion object {
		private const val STUDENT_COLLECTION = "students"
		private const val WORKOUT_COLLECTION = "workouts"
	}

	private suspend fun getWorkouts(studentId: String): List<Workout?> {
		return Firebase.firestore
			.collection(WORKOUT_COLLECTION)
			.whereEqualTo("studentId", studentId)
			.get().await().toObjects<Workout>()
	}
}