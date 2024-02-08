package com.shaunyarbrough.laptracks.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shaunyarbrough.laptracks.data.StudentRepository
import com.shaunyarbrough.laptracks.ui.views.StudentDetailsDestination

class StudentViewModel(
  savedStateHandle: SavedStateHandle,
 private val studentRepository: StudentRepository): ViewModel() {

   private val studentId: Int = checkNotNull(savedStateHandle[StudentDetailsDestination.studentIdArg])


}