package com.shaunyarbrough.laptracks.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.shaunyarbrough.laptracks.LapTrackApplication
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetailsViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentEntryViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentListViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutViewModel

object AppViewModelProvider {
  val Factory = viewModelFactory {
    initializer {
      WorkoutViewModel(
        lapTrackApplication().container.studentWorkoutRepository,
      )
    }

    initializer {
      StudentEntryViewModel(
        lapTrackApplication().container.studentWorkoutRepository
      )
    }

    initializer {
      StudentViewModel(
        this.createSavedStateHandle(),
        lapTrackApplication().container.studentWorkoutRepository
      )
    }

    initializer {
      StudentListViewModel(
        lapTrackApplication().container.studentWorkoutRepository
      )
    }

    initializer {
      StudentDetailsViewModel(
        this.createSavedStateHandle(),
        lapTrackApplication().container.studentWorkoutRepository
      )
    }
  }
}

fun CreationExtras.lapTrackApplication(): LapTrackApplication =
  (this[AndroidViewModelFactory.APPLICATION_KEY] as LapTrackApplication)