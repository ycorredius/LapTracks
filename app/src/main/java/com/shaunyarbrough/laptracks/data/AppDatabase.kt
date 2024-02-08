package com.shaunyarbrough.laptracks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Student::class,Workout::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
  abstract fun studentDao(): StudentDao
  abstract fun workoutDao(): WorkoutDao

  companion object{
    @Volatile
    private var Instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
      return Instance?: synchronized(this){
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
          .build()
          .also { Instance = it }
      }
    }
  }
}