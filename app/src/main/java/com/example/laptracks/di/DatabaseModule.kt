package com.example.laptracks.di

import android.content.Context
import com.example.laptracks.data.AppDatabase
import com.example.laptracks.data.OfflineStudentRepository
import com.example.laptracks.data.StudentDao
import com.example.laptracks.data.StudentRepository
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
}