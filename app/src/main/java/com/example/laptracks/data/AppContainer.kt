package com.example.laptracks.data

import android.content.Context
import com.example.laptracks.data.AppDatabase.Companion.getDatabase

interface AppContainer {
  val workoutRepository: WorkoutRepository
  val studentRepository: StudentRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
  override val studentRepository: StudentRepository by lazy {
    OfflineStudentRepository(getDatabase(context).studentDao())
  }

  override val workoutRepository: WorkoutRepository by lazy {
    OfflineWorkoutRepository(getDatabase(context).workoutDao())
  }
}