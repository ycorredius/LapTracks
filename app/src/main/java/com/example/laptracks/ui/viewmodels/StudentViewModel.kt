package com.example.laptracks.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.laptracks.data.StudentRepository
import com.example.laptracks.ui.views.StudentDetailsDestination

class StudentViewModel(
  savedStateHandle: SavedStateHandle,
 private val studentRepository: StudentRepository): ViewModel() {

   private val studentId: Int = checkNotNull(savedStateHandle[StudentDetailsDestination.studentIdArg])


}