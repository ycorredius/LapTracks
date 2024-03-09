package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
class StudentWorkoutRepositoryImpl(
	private val studentRepository: StudentRepository,
	private val workoutRepository: WorkoutRepository
) :
	StudentWorkoutRepository {
	override fun getStudentsStream(): Flow<List<StudentRoom>> {
		return studentRepository.getStudentsStream()
	}

	override fun getStudentsWithWorkoutsStream(): Flow<Map<StudentRoom, List<Workout>>> {
		return studentRepository.getStudentsWithWorkoutsStream()
	}

	override fun getStudent(id: Int): Flow<StudentRoom?> {
		return studentRepository.getStudent(id)
	}

	override fun loadStudentWithWorkouts(id: Int): Flow<Map<StudentRoom, List<Workout>?>> {
		return studentRepository.loadStudentWithWorkouts(id)
	}

	override suspend fun insertStudent(student: StudentRoom) {
		return studentRepository.insertStudent(student)
	}

	override suspend fun updateStudent(student: StudentRoom) {
		return studentRepository.updateStudent(student)
	}

	override suspend fun deleteStudent(student: StudentRoom) {
		return studentRepository.deleteStudent(student)
	}

	override fun getWorkouts(): Flow<List<WorkoutRoom>?> {
		return workoutRepository.getWorkouts()
	}

	override suspend fun insertWorkout(workout: WorkoutRoom) {
		return workoutRepository.insertWorkout(workout)
	}

	override fun getWorkoutWithStudent(id: Int): Flow<WorkoutWithStudent> {
		return workoutRepository.getWorkoutWithStudent(id)
	}
}