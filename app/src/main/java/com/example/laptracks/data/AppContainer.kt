package com.example.laptracks.data

import android.content.Context
import com.example.laptracks.ServiceLocator
import com.example.laptracks.StudentWorkoutRepository

interface AppContainer {
  val studentWorkoutRepository: StudentWorkoutRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
  override val studentWorkoutRepository: StudentWorkoutRepository by lazy {
    ServiceLocator.provideStudentWorkoutRepositoryImpl(context)
  }
}