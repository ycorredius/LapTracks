package com.shaunyarbrough.laptracks.interval

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.shaunyarbrough.laptracks.LapTrackApp
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ServiceLocator
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.StudentRoom
import com.shaunyarbrough.laptracks.data.StudentWorkoutRepository
import com.shaunyarbrough.laptracks.data.source.FakeStudentWorkoutRepository
import com.shaunyarbrough.laptracks.ui.views.IntervalDestination
import com.shaunyarbrough.laptracks.ui.views.ParticipantSummaryDestination
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IntervalTest {
	private lateinit var studentWorkoutRepository: StudentWorkoutRepository
	private lateinit var navController: TestNavHostController

	@get:Rule
	val composeTestRule = createAndroidComposeRule<ComponentActivity>()

	@Before
	fun setup() = runBlocking {
		studentWorkoutRepository = FakeStudentWorkoutRepository()
		ServiceLocator.studentWorkoutRepository = studentWorkoutRepository

		val students = StudentRoom(id=1, firstName = "Billy", lastName = "Smith", displayName = "BSmith")
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
	fun intervalScreen_NextButtonIsInitiallyDisabled(){
		moveToInterval()

		composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.next)).assertIsNotEnabled().performClick()
		assertEquals(navController.currentBackStackEntry?.destination?.route, IntervalDestination.route)
	}

	@Test
	fun intervalScreen_DropDownMenuAppearsWhenClicked(){
		moveToInterval()

		composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.drop_down_button)).performClick()
		composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.drop_down_menu)).assertIsDisplayed()
	}

	@Test
	fun intervalScreen_SelectingAnIntervalEnablesNextButton(){
		moveToInterval()
		selectedInterval()
		composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.next)).assertIsEnabled()
	}

	@Test
	fun intervalScreen_AfterClickingNextButtonPracticeSummaryIsNextScreen(){
		moveToInterval()
		selectedInterval()
		composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.next)).performClick()
		assertEquals(navController.currentBackStackEntry?.destination?.route, ParticipantSummaryDestination.route)
	}

	private fun moveToInterval(){
		composeTestRule.onNodeWithTag("Billy").performClick()
		composeTestRule.onNodeWithTag("Next").performClick()
	}

	private fun selectedInterval(){
		composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.drop_down_button)).performClick()
		composeTestRule.onNodeWithTag("200").performClick()
	}
}