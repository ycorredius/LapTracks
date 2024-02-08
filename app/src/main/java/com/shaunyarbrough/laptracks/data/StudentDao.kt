package com.shaunyarbrough.laptracks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao{
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(student: Student)

  @Update
  suspend fun update(student: Student)

  @Delete
  suspend fun delete(student: Student)

  @Query("SELECT * from students WHERE id = :id")
  fun getStudent(id: Int): Flow<Student>

  @Query("SELECT * from students")
  fun getAllStudents(): Flow<List<Student>>

  @Query("SELECT * from students LEFT JOIN workouts ON students.id = workouts.studentId")
  fun getAllStudentsWithWorkouts(): Flow<Map<Student,List<Workout>>>

  @Query("Select * FROM students LEFT JOIN workouts ON students.id = workouts.studentId WHERE students.id = :id")
  fun loadStudentAndWorkouts(id: Int): Flow<Map<Student,List<Workout>?>>
}