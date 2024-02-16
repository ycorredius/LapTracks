package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
class StudentWorkoutRepositoryImpl(private val studentRepository: StudentRepository, private val workoutRepository: WorkoutRepository):
	StudentWorkoutRepository {
	override fun getStudentsStream(): Flow<List<Student>>{
		return studentRepository.getStudentsStream()
	}

	override fun getStudentsWithWorkoutsStream(): Flow<Map<Student, List<Workout>>>{
		return  studentRepository.getStudentsWithWorkoutsStream()
	}

	override fun getStudent(id: Int): Flow<Student?> {
		return studentRepository.getStudent(id)
	}
	override fun loadStudentWithWorkouts(id: Int): Flow<Map<Student, List<Workout>?>> {
		return studentRepository.loadStudentWithWorkouts(id)
	}

	override suspend fun insertStudent(student: Student) {
		return studentRepository.insertStudent(student)
	}

	override suspend fun updateStudent(student: Student){
		return studentRepository.updateStudent(student)
	}

	override suspend fun deleteStudent(student: Student){
		return studentRepository.deleteStudent(student)
	}

	override fun getWorkouts(): Flow<List<Workout>?> {
		return workoutRepository.getWorkouts()
	}

	override suspend fun insertWorkout(workout: Workout) {
		return workoutRepository.insertWorkout(workout)
	}

}