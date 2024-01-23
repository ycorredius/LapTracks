package com.example.laptracks.data

import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
  fun getWorkout(id: Int): Flow<Workout>

  suspend fun insertWorkouts(workouts: Workout)
}