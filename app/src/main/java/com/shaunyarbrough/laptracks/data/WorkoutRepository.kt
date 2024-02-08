package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
  fun getWorkouts(): Flow<List<Workout>?>

  suspend fun insertWorkout(workout: Workout)
}