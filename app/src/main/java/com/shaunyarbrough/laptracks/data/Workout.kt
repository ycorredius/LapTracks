package com.shaunyarbrough.laptracks.data

data class Workout(
  val id: String = "0",
  val date: String = "",
  val lapList: List<Long> = emptyList(),
  val interval: String = "",
  val studentId: String = "",
  val totalTime: Long = 0L
)
