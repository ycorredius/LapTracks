package com.shaunyarbrough.laptracks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val lapList: List<Long>,
    val interval: String,
    val studentId: Int,
    val totalTime: Long
)