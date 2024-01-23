package com.example.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineStudentRepository @Inject constructor(private val studentDao: StudentDao): StudentRepository {
  override fun getStudentsStream(): Flow<List<Student>> = studentDao.getAllStudents()

  override fun getStudentsWithWorkoutsStream(): Flow<Map<Student, List<Workout>>> = studentDao.getAllStudentsWithWorkouts()

  override fun getStudent(id: Int): Flow<Student?> = studentDao.getStudent(id)
  override fun loadStudentWithWorkouts(id: Int): Flow<Map<Student, List<Workout>?>> = studentDao.loadStudentAndWorkouts(id)

  override suspend fun insertStudent(student: Student) = studentDao.insert(student)

  override suspend fun updateStudent(student: Student) = studentDao.update(student)

  override suspend fun deleteStudent(student: Student) = studentDao.delete(student)
}