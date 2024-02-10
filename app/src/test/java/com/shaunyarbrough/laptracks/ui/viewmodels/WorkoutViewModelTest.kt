package com.shaunyarbrough.laptracks.ui.viewmodels

import com.shaunyarbrough.laptracks.ServiceLocator
import com.shaunyarbrough.laptracks.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.source.FakeStudentWorkoutRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WorkoutViewModelTest {
	//TODO: Finish building out test suite
	private lateinit var studentWorkoutRepository: StudentWorkoutRepository

	private val viewModel =
		WorkoutViewModel(FakeStudentWorkoutRepository())

	@Before
	fun setup() = runBlocking{
		studentWorkoutRepository = FakeStudentWorkoutRepository()
		ServiceLocator.studentWorkoutRepository = studentWorkoutRepository

		val students = Student(id=1, firstName = "Billy", lastName = "Smith", displayName = "BSmith")
		studentWorkoutRepository.insertStudent(students)
	}

	@Test
	fun workoutViewModel_ParticipantListInitiallyZero(){
		assertEquals(viewModel.workoutUiState.value.participantsList.size, 0)
	}

	@Test
	fun workoutViewModel_SetParticipantsAddsParticipantToList() = runBlocking{
		val result = studentWorkoutRepository.getStudentsStream()
		viewModel.setParticipants(result.first().first())
		assertEquals(viewModel.workoutUiState.value.participantsList.size, 1)
	}

	@Test
	fun workoutViewModel_SetInterval(){
		viewModel.setInterval("400")

		assertEquals(viewModel.workoutUiState.value.interval, "400")
	}

	@Test
	fun workoutViewModel_SetParticipantTime() = runBlocking {
		val student = studentWorkoutRepository.getStudent(1).first()
		if (student != null) {
			viewModel.setParticipants(student)
			viewModel.setParticipantTime(student, 50000_000L)
			viewModel.setParticipantTime(student, 60000_000L)
			assertEquals(viewModel.workoutUiState.value.participantsList.getValue(student).size, 2)
		}
	}

	@Test
	fun workoutViewModel_ResetWorkoutUiState() = runBlocking{
		val student = studentWorkoutRepository.getStudent(1).first()
		if (student != null) {
			viewModel.setParticipants(student)
			viewModel.setParticipantTime(student, 50000_000L)
			viewModel.setParticipantTime(student, 60000_000L)
			assertEquals(viewModel.workoutUiState.value.participantsList.getValue(student).size,2)
		}
		viewModel.resetWorkout()
		assertEquals(viewModel.workoutUiState.value.participantsList.size, 0)
	}
}