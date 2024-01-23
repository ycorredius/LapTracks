package com.example.laptracks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val date: String,
  val lapList: List<Long>,
  val interval: String,
  val studentId: Int
)
