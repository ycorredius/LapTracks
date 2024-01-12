package com.example.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.laptracks.data.Student
import com.example.laptracks.data.StudentRepository

class StudentEntryViewModel(private val studentRepository: StudentRepository) : ViewModel(){

  var studentUiState by mutableStateOf(StudentUiState())
    private set

  fun updateUiState(studentDetails: StudentDetails){
    studentUiState = StudentUiState(studentDetails = studentDetails, isStudentValid = validateStudent(studentDetails))
  }

  private fun validateStudent(uiState: StudentDetails = studentUiState.studentDetails): Boolean{
    return with(uiState){
      firstName.isNotBlank() && lastName.isNotBlank() && displayName.isNotBlank()
    }
  }

  suspend fun saveStudent(){
    if (validateStudent()) {
      studentRepository.insertStudent(studentUiState.studentDetails.toStudent())
    }
  }
}

data class StudentUiState(
  val studentDetails: StudentDetails = StudentDetails(),
  val isStudentValid: Boolean = false
)
data class StudentDetails(
  val id: Int = 0,
  val firstName: String = "",
  val lastName: String = "",
  val displayName: String = ""
)

fun StudentDetails.toStudent(): Student = Student(
  id = id,
  firstName = firstName,
  lastName = lastName,
  displayName = displayName
)