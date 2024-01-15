package com.example.laptracks.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.laptracks.LapTrackApplication
import com.example.laptracks.ui.viewmodels.StudentEntryViewModel
import com.example.laptracks.ui.viewmodels.WorkoutViewModel

object AppViewModelProvider {
  val Factory = viewModelFactory {
   initializer {
      WorkoutViewModel(
        lapTrackApplication().container.studentRepository)
   }

    initializer {
      StudentEntryViewModel(
        lapTrackApplication().container.studentRepository)
    }
  }
}

fun CreationExtras.lapTrackApplication() : LapTrackApplication =
  (this[AndroidViewModelFactory.APPLICATION_KEY] as LapTrackApplication)