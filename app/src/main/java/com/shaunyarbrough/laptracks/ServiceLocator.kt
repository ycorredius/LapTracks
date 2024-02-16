package com.shaunyarbrough.laptracks

import android.content.Context
import androidx.room.Room
import com.shaunyarbrough.laptracks.data.AppDatabase
import com.shaunyarbrough.laptracks.data.OfflineStudentRepository
import com.shaunyarbrough.laptracks.data.OfflineWorkoutRepository
import com.shaunyarbrough.laptracks.data.StudentRepository
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepositoryImpl
import com.shaunyarbrough.laptracks.data.WorkoutRepository
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.VisibleForTesting

object ServiceLocator {
	private val lock = Any()
	private var database: AppDatabase? = null

	@Volatile
	var studentWorkoutRepository: StudentWorkoutRepository? = null
		@VisibleForTesting set

	@VisibleForTesting
	fun resetRepository(){
		synchronized(lock){
			runBlocking {

			}
			database?.apply {
				clearAllTables()
				close()
			}
			database = null
			studentWorkoutRepository = null
		}
	}

	fun provideStudentWorkoutRepositoryImpl(context: Context): StudentWorkoutRepository {
		synchronized(this){
			return studentWorkoutRepository ?: createStudentWorkoutRepository(context)
		}
	}

	private fun createDataBase(context: Context): AppDatabase {
		val result = Room.databaseBuilder(
			context.applicationContext,
			AppDatabase::class.java, "app_database"
		).build()
		database = result
		return result
	}

	private fun createStudentWorkoutRepository(context: Context): StudentWorkoutRepository{
		val newRepo = StudentWorkoutRepositoryImpl(
			createStudentRepository(context),
			createWorkoutRepository(context)
		)
		studentWorkoutRepository = newRepo
		return newRepo
	}

	private fun createStudentRepository(context: Context): StudentRepository {
		val database = database ?: createDataBase(context)
		return OfflineStudentRepository(database.studentDao())
	}

	private fun createWorkoutRepository(context: Context): WorkoutRepository {
		val database = database ?: createDataBase(context)
		return OfflineWorkoutRepository(database.workoutDao())
	}

}