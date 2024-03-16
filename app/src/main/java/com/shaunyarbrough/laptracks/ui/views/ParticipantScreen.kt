package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentsUiState
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamUiState
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
) {
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
	val workoutUiState by workoutViewModel.workoutUiState.collectAsState()
	val teamUiState = workoutViewModel.teamUiState


	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(ParticipantDestination.titleRes),
				canNavigateBack = false,
				scrollBehavior = scrollBehavior
			)
		},
	) { innerPadding ->
		val scope = rememberCoroutineScope()
		Box(modifier = Modifier.padding(innerPadding)) {
			when (teamUiState) {
				is TeamUiState.Success -> ParticipantBody(
					participants = workoutUiState.participantsList,
					onCheckBoxChange = { workoutViewModel.setParticipants(it) },
					navigateToInterval = navigateToInterval,
					teams = teamUiState.teams,
					updateStudents = workoutViewModel::getStudents,
					studentUiState = workoutViewModel.studentsUiState
				)

				is TeamUiState.Loading -> LoadingScreen()
				is TeamUiState.Error -> Text(text = "Something went wrong!")
			}

		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantBody(
	participants: Map<Student, List<Long>>,
	teams: List<Team>,
	studentUiState: StudentsUiState,
	navigateToInterval: () -> Unit,
	onCheckBoxChange: (Student) -> Unit,
	updateStudents: (String) -> Unit = {}
) {
	var selectedTeam by remember { mutableStateOf(Pair(teams.first().name, teams.first().id)) }
	var isExpanded by remember { mutableStateOf(false) }
	Column(
		modifier = Modifier
			.padding(20.dp)
			.fillMaxSize(),
	) {
		ExposedDropdownMenuBox(
			expanded = isExpanded,
			onExpandedChange = { isExpanded = it }) {
			OutlinedTextField(
				value = selectedTeam.first,
				onValueChange = {},
				readOnly = true,
				label = {
					Text(
						text = "Select team"
					)
				},
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
				modifier = Modifier
					.fillMaxWidth()
					.menuAnchor()
			)
			ExposedDropdownMenu(
				expanded = isExpanded,
				onDismissRequest = { isExpanded = !isExpanded }) {
				teams.forEach { team ->
					DropdownMenuItem(
						text = { Text(text = team.name) },
						onClick = {
							selectedTeam = Pair(team.name, team.id)
							isExpanded = !isExpanded
							updateStudents(team.id)
						})
				}
			}
		}
		Divider(modifier = Modifier.padding(0.dp, 10.dp))
		when (studentUiState) {
			is StudentsUiState.Loading -> Text(text = "Students loading")
			is StudentsUiState.Success -> Participants(
				students = studentUiState.students,
				participants = participants,
				onCheckBoxChange = onCheckBoxChange
			)
			is StudentsUiState.Error -> Text(text = "Something went wrong")
		}
		Spacer(modifier = Modifier.weight(1f))
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
	studentList: List<Student?>,
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
							if (student != null) {
								onCheckBoxChange(student)
							}
						},
					shape = MaterialTheme.shapes.extraSmall,
					colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)

				) {
					Box(modifier = Modifier.padding(15.dp)) {
						if (student != null) {
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
}

@Composable
fun Participants(
	students: List<Student?>,
	participants: Map<Student, List<Long>>,
	onCheckBoxChange: (Student) -> Unit,

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
			participants = participants
		) { onCheckBoxChange(it) }
	}

}

@Preview
@Composable
fun ParticipantScreenPreview() {
	ParticipantBody(
		participants = mapOf(
			Student(
				id = "",
				firstName = "Billy",
				lastName = "Smith",
				displayName = "BSmith",
				teamId = "Some id"
			) to listOf(1_000L, 2_000L)
		),
		onCheckBoxChange = { /* nothing */ },
		navigateToInterval = { /* nothing */ },
		teams = emptyList(),
		studentUiState = StudentsUiState.Loading
	)
}

@Preview(showBackground = true)
@Composable
fun ParticipantEmptyScreenPreview() {
	LapTracksTheme {
		ParticipantBody(
			participants = emptyMap(),
			onCheckBoxChange = { /* nothing */ },
			navigateToInterval = { /* nothing */ },
			teams = emptyList(),
			studentUiState = StudentsUiState.Loading
		)
	}

}