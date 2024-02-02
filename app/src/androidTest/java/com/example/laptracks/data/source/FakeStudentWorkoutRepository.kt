package com.example.laptracks.data.source

import com.example.laptracks.StudentWorkoutRepository
import com.example.laptracks.data.Student
import com.example.laptracks.data.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeStudentWorkoutRepository: StudentWorkoutRepository {
	private var students: MutableList<Student> = mutableListOf()
	private var workouts: MutableList<Workout> = mutableListOf()

	override fun getStudentsStream(): Flow<List<Student>> = flow {
		emit(ArrayList(students))
	}

	override fun getStudentsWithWorkoutsStream(): Flow<Map<Student, List<Workout>>> {
		TODO("Not yet implemented")
	}

	override fun getStudent(id: Int): Flow<Student?> {
		TODO("Not yet implemented")
	}

	override fun loadStudentWithWorkouts(id: Int): Flow<Map<Student, List<Workout>?>> {
		TODO("Not yet implemented")
	}

	override suspend fun insertStudent(student: Student) {
		students.add(student)
	}

	override suspend fun updateStudent(student: Student) {
		TODO("Not yet implemented")
	}

	override suspend fun deleteStudent(student: Student) {
		students.clear()
	}

	override fun getWorkouts(): Flow<List<Workout>> = flow {
		emit(ArrayList(workouts))
	}

	override suspend fun insertWorkout(workout: Workout) {
		workouts.add(workout)
	}
}