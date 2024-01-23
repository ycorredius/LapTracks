package com.example.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineWorkoutRepository @Inject constructor(private val workoutDao: WorkoutDao) : WorkoutRepository {
  override fun getWorkout(id: Int): Flow<Workout>  = workoutDao.getWorkout(id)

  override suspend fun insertWorkouts(workouts: Workout) = workoutDao.workoutInserts(workouts)

}