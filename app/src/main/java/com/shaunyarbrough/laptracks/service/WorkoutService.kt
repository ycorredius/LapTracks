package com.shaunyarbrough.laptracks.service

import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Workout

interface WorkoutService {
    suspend fun getWorkout(id: String): Map<Student?,Workout?>
    suspend fun createWorkout(workout: Workout)
    suspend fun deleteWorkout(workoutId: String)
}