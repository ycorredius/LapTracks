package com.shaunyarbrough.laptracks.data

data class StudentWithWorkout(
    val id: String = "0",
    val date: String,
    val lapList: List<Long>,
    val interval: String,
    val totalTime: Long,
    val student: Student
)
