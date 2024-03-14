package com.shaunyarbrough.laptracks.data.source

import com.shaunyarbrough.laptracks.data.StudentRoom
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.data.WorkoutRoom
import com.shaunyarbrough.laptracks.data.WorkoutWithStudent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeStudentWorkoutRepository: StudentWorkoutRepository {
	private var students: MutableList<StudentRoom> = mutableListOf()
	private var workouts: MutableList<WorkoutRoom> = mutableListOf()

	override fun getStudentsStream(): Flow<List<StudentRoom>> = flow {
		emit(ArrayList(students))
	}

	override fun getStudentsWithWorkoutsStream(): Flow<Map<StudentRoom, List<Workout>>> {
		TODO("Not yet implemented")
	}

	override fun getStudent(id: Int): Flow<StudentRoom> = flow {
		students.find { it.id == id }?.let { emit(it) }
	}

	override fun loadStudentWithWorkouts(id: Int): Flow<Map<StudentRoom, List<Workout>?>> {
		TODO("Not yet implemented")
	}

	override suspend fun insertStudent(student: StudentRoom) {
		students.add(student)
	}

	override suspend fun updateStudent(student: StudentRoom) {
		TODO("Not yet implemented")
	}

	override suspend fun deleteStudent(student: StudentRoom) {
		students.clear()
	}

	override fun getWorkouts(): Flow<List<WorkoutRoom>> = flow {
		emit(ArrayList(workouts))
	}

	override suspend fun insertWorkout(workout: WorkoutRoom) {
		workouts.add(workout)
	}

	override fun getWorkoutWithStudent(id: Int): Flow<WorkoutWithStudent> {
		TODO("Not yet implemented")
	}
}