package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shaunyarbrough.laptracks.LapTrackAppBottomAppBar
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutViewModel

object ParticipantDestination : NavigationDestination {
	override val route = "participants"
	override val titleRes = R.string.participants
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantScreen(
	navigateToInterval: () -> Unit,
	workoutViewModel: WorkoutViewModel,
	navController: NavHostController
) {
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
	val workoutUiState by workoutViewModel.workoutUiState.collectAsState()
	val studentUiState by workoutViewModel.studentsUiState.collectAsState()
	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(ParticipantDestination.titleRes),
				canNavigateBack = false,
				scrollBehavior = scrollBehavior
			)
		},
		bottomBar = { LapTrackAppBottomAppBar(navController =  navController ) }
	) { innerPadding ->
		Box(modifier = Modifier.padding(innerPadding)) {
			ParticipantBody(
				participants = workoutUiState.participantsList,
				students = studentUiState.studentsList,
				onCheckBoxChange = { workoutViewModel.setParticipants(it) },
				navigateToInterval = navigateToInterval,
			)
		}
	}
}

@Composable
fun ParticipantBody(
	participants: Map<Student, List<Long>>,
	students: List<Student>,
	navigateToInterval: () -> Unit,
	onCheckBoxChange: (Student) -> Unit = {}
) {
	Column(
		modifier = Modifier
			.padding(20.dp)
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceBetween
	) {
		if (students.isEmpty()) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = stringResource(id = R.string.no_students),
					textAlign = TextAlign.Center,
					fontSize = 24.sp,
				)
			}

		} else {
			ParticipantList(
				studentList = students,
				participants = participants,
				onCheckBoxChange = { onCheckBoxChange(it) }
			)
		}
		Button(
			onClick = { navigateToInterval() },
			enabled = participants.isNotEmpty(),
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp, 0.dp)
				.testTag(stringResource(id = R.string.next))
		) {
			Text(
				stringResource(R.string.next),
				fontSize = dimensionResource(id = R.dimen.button_font).value.sp
			)
		}
	}
}

@Composable
private fun ParticipantList(
	studentList: List<Student>,
	participants: Map<Student, List<Long>>,
	onCheckBoxChange: (Student) -> Unit
) {
	LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
		items(studentList) { student ->
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.border(
						width = 2.dp,
						color = if (participants.containsKey(student))
							MaterialTheme.colorScheme.secondary else
							MaterialTheme.colorScheme.secondaryContainer
					)
			) {
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							onCheckBoxChange(student)
						},
					shape = MaterialTheme.shapes.extraSmall,
					colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)

				) {
					Box(modifier = Modifier.padding(15.dp)) {
						Text(
							text = student.displayName,
							fontSize = 18.sp
						)
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun ParticipantScreenPreview() {
	ParticipantBody(
		participants = mapOf(
			Student(
				id = 0,
				firstName = "Billy",
				lastName = "Smith",
				displayName = "BSmith"
			) to listOf(1_000L, 2_000L)
		),
		students = listOf(
			Student(firstName = "Billy", lastName = "Smith", displayName = "BSmith")
		),
		onCheckBoxChange = { /* nothing */ },
		navigateToInterval = { /* nothing */ },
	)
}

@Preview(showBackground = true)
@Composable
fun ParticipantEmptyScreenPreview() {
	LapTracksTheme {
		ParticipantBody(
			participants = emptyMap(),
			students = emptyList(),
			onCheckBoxChange = { /* nothing */ },
			navigateToInterval = { /* nothing */ }
		)
	}

}