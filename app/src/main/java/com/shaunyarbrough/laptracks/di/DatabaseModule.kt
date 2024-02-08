package com.shaunyarbrough.laptracks.di

import android.content.Context
import com.shaunyarbrough.laptracks.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.AppDatabase
import com.shaunyarbrough.laptracks.data.OfflineStudentRepository
import com.shaunyarbrough.laptracks.data.OfflineWorkoutRepository
import com.shaunyarbrough.laptracks.data.StudentDao
import com.shaunyarbrough.laptracks.data.StudentRepository
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepositoryImpl
import com.shaunyarbrough.laptracks.data.WorkoutDao
import com.shaunyarbrough.laptracks.data.WorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

  @Provides
  @Singleton
  fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
    return AppDatabase.getDatabase(context)
  }

  @Provides
  @Singleton
  fun provideStudentDao(database: AppDatabase): StudentDao{
    return database.studentDao()
  }

  @Provides
  @Singleton
  fun provideStudentRepository(studentDao: StudentDao) :StudentRepository{
    return OfflineStudentRepository(studentDao)
  }

  @Provides
  @Singleton
  fun provideWorkoutDao(database: AppDatabase): WorkoutDao{
    return database.workoutDao()
  }

  @Provides
  @Singleton
  fun provideWorkoutRepository(workoutDao: WorkoutDao) : WorkoutRepository{
    return  OfflineWorkoutRepository(workoutDao)
  }

  @Provides
  @Singleton
  fun provideStudentWorkoutRepositoryImpl(studentRepository: StudentRepository, workoutRepository: WorkoutRepository): StudentWorkoutRepository{
    return StudentWorkoutRepositoryImpl(studentRepository,workoutRepository)
  }
}