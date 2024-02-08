package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow

interface StudentRepository{

  fun getStudentsStream(): Flow<List<Student>>

  fun getStudentsWithWorkoutsStream():Flow<Map<Student, List<Workout>>>

  fun getStudent(id: Int): Flow<Student?>

  fun loadStudentWithWorkouts(id: Int): Flow<Map<Student, List<Workout>?>>

  suspend fun insertStudent(student: Student)

  suspend fun updateStudent(student: Student)

  suspend fun deleteStudent(student: Student)
}