package com.shaunyarbrough.laptracks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Workout(
  val id: String = "0",
  val date: String,
  val lapList: List<Long>,
  val interval: String,
  val studentId: String,
  val totalTime: Long
)
