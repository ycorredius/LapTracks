package com.shaunyarbrough.laptracks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
  @Insert(onConflict  = OnConflictStrategy.REPLACE)
  fun workoutInserts(vararg workouts: WorkoutRoom)

  @Query("SELECT * FROM workouts")
  fun getWorkouts(): Flow<List<WorkoutRoom>?>

  @Transaction
  @Query("SELECT workouts.*, students.* FROM workouts JOIN students ON workouts.studentId = studentId WHERE workouts.id = :id ")
  fun getWorkoutWithStudent(id: Int): Flow<WorkoutWithStudent>
}