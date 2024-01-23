package com.example.laptracks.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laptracks.data.Student
import com.example.laptracks.data.StudentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StudentListViewModel(studentRepository: StudentRepository): ViewModel() {
  val studentListUiState: StateFlow<StudentListUiState> =
    studentRepository.getStudentsStream().map { StudentListUiState(it) }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StudentListUiState()
      )
}

data class StudentListUiState(
  val studentList: List<Student> = listOf()
)