package com.shaunyarbrough.laptracks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
	@PrimaryKey(autoGenerate = true)
  val id: Int = 0,
	val firstName: String,
	val lastName: String,
	val displayName: String
)
