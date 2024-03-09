package com.shaunyarbrough.laptracks.service

import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Workout
import kotlinx.coroutines.flow.Flow

interface StudentService {
    val students: Flow<List<Student>>
    suspend fun getStudent(id: String): Student?
    suspend fun getStudentWithWorkouts(id: String): Map<Student,List<Workout>>
    suspend fun createStudent(student: Student)
    suspend fun updateStudent(student: Student)
    suspend fun deleteStudent(id: String)
}