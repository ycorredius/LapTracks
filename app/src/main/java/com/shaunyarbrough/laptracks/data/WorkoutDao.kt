package com.shaunyarbrough.laptracks.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
  @Insert(onConflict  = OnConflictStrategy.REPLACE)
  fun workoutInserts(vararg workouts: Workout)

  @Query("SELECT * FROM workouts")
  fun getWorkouts(): Flow<List<Workout>?>
}