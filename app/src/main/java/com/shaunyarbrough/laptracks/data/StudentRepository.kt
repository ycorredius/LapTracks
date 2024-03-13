package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow

interface StudentRepository{

  fun getStudentsStream(): Flow<List<StudentRoom>>

  fun getStudentsWithWorkoutsStream():Flow<Map<StudentRoom, List<Workout>>>

  fun getStudent(id: Int): Flow<StudentRoom?>

  fun loadStudentWithWorkouts(id: Int): Flow<Map<StudentRoom, List<Workout>?>>

  suspend fun insertStudent(student: StudentRoom)

  suspend fun updateStudent(student: StudentRoom)

  suspend fun deleteStudent(student: StudentRoom)
}