package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
  fun getWorkouts(): Flow<List<WorkoutRoom>?>

  suspend fun insertWorkout(workout: WorkoutRoom)

  fun getWorkoutWithStudent(id: Int): Flow<WorkoutWithStudent>
}