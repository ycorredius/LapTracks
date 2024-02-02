package com.example.laptracks

import com.example.laptracks.data.StudentRepository
import com.example.laptracks.data.WorkoutRepository

interface StudentWorkoutRepository : StudentRepository, WorkoutRepository {
}