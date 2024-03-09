package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineStudentRepository @Inject constructor(private val studentDao: StudentDao): StudentRepository {
  override fun getStudentsStream(): Flow<List<StudentRoom>> = studentDao.getAllStudents()

  override fun getStudentsWithWorkoutsStream(): Flow<Map<StudentRoom, List<Workout>>> = studentDao.getAllStudentsWithWorkouts()

  override fun getStudent(id: Int): Flow<StudentRoom?> = studentDao.getStudent(id)
  override fun loadStudentWithWorkouts(id: Int): Flow<Map<StudentRoom, List<Workout>?>> = studentDao.loadStudentAndWorkouts(id)

  override suspend fun insertStudent(student: StudentRoom) = studentDao.insert(student)

  override suspend fun updateStudent(student: StudentRoom) = studentDao.update(student)

  override suspend fun deleteStudent(student: StudentRoom) = studentDao.delete(student)
}