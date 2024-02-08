package com.shaunyarbrough.laptracks.data

import android.content.Context
import com.shaunyarbrough.laptracks.ServiceLocator
import com.shaunyarbrough.laptracks.StudentWorkoutRepository

interface AppContainer {
  val studentWorkoutRepository: StudentWorkoutRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
  override val studentWorkoutRepository: StudentWorkoutRepository by lazy {
    ServiceLocator.provideStudentWorkoutRepositoryImpl(context)
  }
}