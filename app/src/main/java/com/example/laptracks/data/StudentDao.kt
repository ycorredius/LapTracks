package com.example.laptracks.data

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
}