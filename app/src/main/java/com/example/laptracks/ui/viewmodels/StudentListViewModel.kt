package com.example.laptracks.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptracks.data.Student
import com.example.laptracks.data.StudentRepository
import com.example.laptracks.data.Workout
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StudentListViewModel(studentRepository: StudentRepository): ViewModel() {
  val studentListUiState: StateFlow<StudentListUiState> =
    studentRepository. getStudentsWithWorkoutsStream().map { StudentListUiState(mapToStudentUiModel(it)) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StudentListUiState()
      )
}

fun mapToStudentUiModel(studentMap: Map<Student, List<Workout>>): List<StudentUiModel>{
  val newList = studentMap.entries.toList()
  return newList.map {
    item ->
    StudentUiModel(item.key,item.value)
  }
}

data class StudentListUiState(
  val studentList: List<StudentUiModel> = emptyList()
)

data class StudentUiModel(
  val student: Student,
  val workouts: List<Workout>
)