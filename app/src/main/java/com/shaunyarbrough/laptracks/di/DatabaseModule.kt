package com.shaunyarbrough.laptracks.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.shaunyarbrough.laptracks.data.AppDatabase
import com.shaunyarbrough.laptracks.data.AuthRepository
import com.shaunyarbrough.laptracks.data.AuthService
import com.shaunyarbrough.laptracks.data.OfflineStudentRepository
import com.shaunyarbrough.laptracks.data.OfflineWorkoutRepository
import com.shaunyarbrough.laptracks.data.StudentDao
import com.shaunyarbrough.laptracks.data.StudentRepository
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepositoryImpl
import com.shaunyarbrough.laptracks.data.WorkoutDao
import com.shaunyarbrough.laptracks.data.WorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"

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
  fun provideAuthRepository(authService: AuthService, dataStore: DataStore<Preferences>): AuthRepository{
    return AuthRepository(authService,dataStore)
  }

  @Provides
  @Singleton
  fun provideStudentWorkoutRepositoryImpl(studentRepository: StudentRepository, workoutRepository: WorkoutRepository): StudentWorkoutRepository {
    return StudentWorkoutRepositoryImpl(studentRepository,workoutRepository)
  }

  @Provides
  @Singleton
  fun providePreferenceDataStore(@ApplicationContext appContext: Context): DataStore<Preferences>{
    return PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences()}
      ),
      migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES_NAME)),
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
      produceFile = {appContext.preferencesDataStoreFile(USER_PREFERENCES_NAME)}
    )
  }

  @Provides
  fun provideAuthService(retrofit: Retrofit): AuthService{
      return retrofit.create(AuthService::class.java)
  }
}