package com.shaunyarbrough.laptracks.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineWorkoutRepository @Inject constructor(private val workoutDao: WorkoutDao) : WorkoutRepository {
  override fun getWorkouts(): Flow<List<WorkoutRoom>?>  = workoutDao.getWorkouts()

  override suspend fun insertWorkout(workout: WorkoutRoom) = workoutDao.workoutInserts(workout)

  override fun getWorkoutWithStudent(id: Int): Flow<WorkoutWithStudent> = workoutDao.getWorkoutWithStudent(id)
}