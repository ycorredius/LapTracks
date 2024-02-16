package com.shaunyarbrough.laptracks.data

import android.content.Context
import com.shaunyarbrough.laptracks.ServiceLocator

//This is irrelavant code because I am using dagger. I am afraid to remove it now
//Will revist this later to remove.
interface AppContainer {
  val studentWorkoutRepository: StudentWorkoutRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
  override val studentWorkoutRepository: StudentWorkoutRepository by lazy {
    ServiceLocator.provideStudentWorkoutRepositoryImpl(context)
  }
}