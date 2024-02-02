package com.example.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineWorkoutRepository @Inject constructor(private val workoutDao: WorkoutDao) : WorkoutRepository {
  override fun getWorkouts(): Flow<List<Workout>?>  = workoutDao.getWorkouts()

  override suspend fun insertWorkout(workout: Workout) = workoutDao.workoutInserts(workout)

}