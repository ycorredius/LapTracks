package com.example.laptracks.participants

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.laptracks.LapTrackApp
import com.example.laptracks.ServiceLocator
import com.example.laptracks.StudentWorkoutRepository
import com.example.laptracks.data.Student
import com.example.laptracks.data.source.FakeStudentWorkoutRepository
import com.example.laptracks.ui.views.IntervalDestination
import com.example.laptracks.ui.views.ParticipantDestination
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ParticipantsScreenTest {
	private lateinit var studentWorkoutRepository: StudentWorkoutRepository
	private lateinit var navController: TestNavHostController

	@get:Rule(order = 1)
	val composeTestRule = createAndroidComposeRule<ComponentActivity>()

	@Before()
	fun setup() = runBlocking {
		studentWorkoutRepository = FakeStudentWorkoutRepository()
		ServiceLocator.studentWorkoutRepository = studentWorkoutRepository

		val students = Student(id=1, firstName = "Billy", lastName = "Smith", displayName = "BSmith")
		studentWorkoutRepository.insertStudent(students)

		composeTestRule.waitForIdle()

		composeTestRule.setContent {
			navController = TestNavHostController(LocalContext.current).apply {
				navigatorProvider.addNavigator(ComposeNavigator())
			}
			LapTrackApp(navController)
		}
	}

	@After
	fun cleanupDb() = runBlocking {
		ServiceLocator.resetRepository()
	}

	@Test
	fun verifyStartingDestination_ParticipantScreen(){
		assertEquals(navController.currentBackStackEntry?.destination?.route, ParticipantDestination.route)
	}

	@Test
	fun participantScreen_VerifyNextButtonIsDisableWithoutParticipant(){
		composeTestRule.onNodeWithTag("Next").assertIsNotEnabled()
	}

	@Test
	fun participantScreen_NextButtonIsEnabledWithParticipantChecked(){
		composeTestRule.onNodeWithTag("Billy").performClick()
		composeTestRule.onNodeWithTag("Next").assertIsEnabled()
	}

	@Test
	fun participantScreen_NextButtonMovesToIntervalScreen(){
		composeTestRule.onNodeWithTag("Billy").performClick()
		composeTestRule.onNodeWithTag("Next").performClick()
		assertEquals(navController.currentBackStackEntry?.destination?.route, IntervalDestination.route)
	}

	private fun loadStudents() = runBlocking{
		studentWorkoutRepository = FakeStudentWorkoutRepository()
		ServiceLocator.studentWorkoutRepository = studentWorkoutRepository

		val students = Student(id=1, firstName = "Billy", lastName = "Smith", displayName = "BSmith")
		studentWorkoutRepository.insertStudent(students)
	}

	private fun moveToInterval(){
		loadStudents()

		composeTestRule.waitForIdle()
		composeTestRule.onNodeWithTag("Billy").performClick()
		composeTestRule.onNodeWithTag("Next").performClick()
		assertEquals(navController.currentBackStackEntry?.destination?.route, IntervalDestination.route)
	}
}