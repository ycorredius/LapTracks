package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.StudentRepository
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.service.StudentService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StudentListViewModel @Inject constructor(
    studentService: StudentService
) : ViewModel() {
    val studentListUiState: StateFlow<StudentListUiState> = studentService.students.map {
        StudentListUiState(
            mapToStudentUiModel(it)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = StudentListUiState()
    )
}

fun mapToStudentUiModel(student: List<Student>): List<StudentUiModel> {
    return student.map { item ->
        StudentUiModel(item)
    }
}

data class StudentListUiState(
    val studentList: List<StudentUiModel> = emptyList()
)

data class StudentUiModel(
    val student: Student,
    val workouts: List<Workout> = emptyList()
)