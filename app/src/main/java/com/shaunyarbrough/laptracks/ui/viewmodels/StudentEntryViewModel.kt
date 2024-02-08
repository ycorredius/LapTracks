package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shaunyarbrough.laptracks.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.Student

class StudentEntryViewModel(private val studentWorkoutRepository: StudentWorkoutRepository) : ViewModel(){

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
      studentWorkoutRepository.insertStudent(studentUiState.studentDetails.toStudent())
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
  val displayName: String = "",
  val workouts: Int = 0
)

fun StudentDetails.toStudent(): Student = Student(
  id = id,
  firstName = firstName,
  lastName = lastName,
  displayName = displayName,
)

//TODO: This will be implemented and used soon.
//fun Student.toStudentUiState(isStudentValid: Boolean = false): StudentUiState = StudentUiState(
//  studentDetails = this.toStudentDetails(),
//  isStudentValid =  isStudentValid
//)

fun Student.toStudentDetails(): StudentDetails = StudentDetails(
  id = id,
  firstName = firstName,
  lastName = lastName,
  displayName = displayName
)