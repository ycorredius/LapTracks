package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.StudentWithWorkouts
import com.shaunyarbrough.laptracks.data.Workout
import com.shaunyarbrough.laptracks.getLapTimeAverage
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetailsUiState
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetailsViewModel
import kotlinx.coroutines.launch

object StudentDetailsDestination : NavigationDestination {
	override val route = "student_details"
	override val titleRes = R.string.student_details
	const val studentIdArg = "studentId"
	val routeWithArg = "$route/{$studentIdArg}"
}

@Composable
fun StudentDetailsScreen(
	viewModel: StudentDetailsViewModel = hiltViewModel(),
	navigateToStudentEdit: (String) -> Unit,
	navigateUp: () -> Unit,
	navigateToStudentList: () -> Unit,
	navigateToWorkoutDetails: (String) -> Unit,
) {
	val studentDetailsUiState = viewModel.uiState.collectAsStateWithLifecycle()
	val scope = rememberCoroutineScope()
	when (val uiState = studentDetailsUiState.value) {
		is StudentDetailsUiState.Success -> StudentDetailsBody(
			uiState.studentDetails,
			onConfirmation = {
				scope.launch {
					viewModel.removeUser(uiState.studentDetails.id)
					navigateUp()
				}
			},
			navigateToStudentList = navigateToStudentList,
			navigateToWorkoutDetails = navigateToWorkoutDetails,
			navigateToStudentEdit = navigateToStudentEdit,
			navigateUp = navigateUp
		)

		is StudentDetailsUiState.Loading -> LoadingScreen()
		is StudentDetailsUiState.Error -> Text(text = "Something went wrong!")
	}

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailsBody(
	student: StudentWithWorkouts,
	onConfirmation: () -> Unit,
	navigateToStudentList: () -> Unit,
	modifier: Modifier = Modifier,
	navigateToWorkoutDetails: (String) -> Unit,
	navigateToStudentEdit: (String) -> Unit,
	navigateUp: () -> Unit
) {
	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(id = StudentDetailsDestination.titleRes),
				canNavigateBack = true,
				navigateUp = navigateUp
			)
		},
		floatingActionButton = {
			FloatingActionButton(onClick = { navigateToStudentEdit(student.id) }) {
				Icon(Icons.Filled.Edit, contentDescription = "Edit button")
			}
		},
		modifier = Modifier.padding(10.dp)
	) { innerPadding ->
		var dialogOpen by remember { mutableStateOf(false) }
		when {
			dialogOpen -> {
				DeleteDialog(
					onDismissRequest = { dialogOpen = false },
					onConfirmation = onConfirmation,
					navigateToStudentList
				)
			}
		}
		Column(modifier = modifier.padding(innerPadding)) {
			Row(
				horizontalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxWidth()
			) {
				Column {
					Text(
						text = "${student.firstName} ${student.lastName}",
						style = MaterialTheme.typography.titleLarge
					)
					Text(text = student.displayName, style = MaterialTheme.typography.titleMedium)
				}
				Icon(
					Icons.Filled.Delete,
					contentDescription = "Delete button",
					Modifier.clickable { dialogOpen = true })
			}

			Divider(modifier = Modifier.padding(0.dp, 10.dp))
			Column {
				Text(text = "Workouts", style = MaterialTheme.typography.titleMedium)
				if (student.workouts.isNotEmpty()) {
					LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
						item {
							Row(modifier = Modifier.padding(10.dp)) {
								TableHeader(text = "Date", weight = 0.3f)
								TableHeader(text = "Lap", weight = 0.2f)
								TableHeader(text = "Average Lap", weight = 0.3f)
							}
						}
						items(student.workouts) { workout ->
							WorkoutItem(
								workout,
								navigateToWorkoutDetails
							)
						}
					}
				} else {
					Text(text = "Sorry no workout data collected.")
				}
			}
		}
	}
}

@Composable
fun WorkoutItem(
	workout: Workout?,
	navigateToWorkoutDetails: (String) -> Unit
) {
	Card(shape = MaterialTheme.shapes.extraSmall, modifier = Modifier.clickable {
		workout?.id?.let {
			navigateToWorkoutDetails(
				it
			)
		}
	}) {
		Row(
			modifier = Modifier
				.padding(10.dp)
				.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
		) {
			workout?.let {
				TableCell(text = workout.date, weight = 0.3f)
				TableCell(text = "${workout.lapList.size}", weight = 0.2f)
				TableCell(text = getLapTimeAverage(workout.lapList), weight = 0.3f)
			}
		}
	}
}

@Composable
fun RowScope.TableCell(
	text: String,
	weight: Float
) {
	Text(
		text = text,
		modifier = Modifier.weight(weight),
		style = MaterialTheme.typography.bodyMedium
	)
}

@Composable
fun RowScope.TableHeader(
	text: String,
	weight: Float
) {
	Text(
		text = text,
		modifier = Modifier.weight(weight),
		style = MaterialTheme.typography.bodyLarge
	)
}

@Composable
private fun DeleteDialog(
	onDismissRequest: () -> Unit,
	onConfirmation: () -> Unit,
	navigateToStudentList: () -> Unit
) {
	AlertDialog(
		icon = {
			Icon(imageVector = Icons.Default.Info, contentDescription = "Image icon")
		},
		onDismissRequest = { onDismissRequest() },
		confirmButton = {
			TextButton(
				onClick = {
					onDismissRequest()
					onConfirmation()
					navigateToStudentList()
				}) {
				Text(text = "Confirm")
			}
		},
		title = {
			Text(text = "Are you sure?")
		},
		text = {
			Text(
				text = "You are about to permanently remove a student. This action can't be undone.",
				textAlign = TextAlign.Center
			)
		},
		dismissButton = {
			TextButton(onClick = { onDismissRequest() }) {
				Text(text = "Cancel")
			}
		}
	)
}

@Preview
@Composable
fun StudentDetailsPreview() {
	LapTracksTheme {
		StudentDetailsBody(
			student = StudentWithWorkouts(
				firstName = "billy",
				lastName = "smith",
				displayName = "BSmith",
				teamId = "some team",
				workouts = emptyList()
			),
			onConfirmation = {},
			navigateToStudentList = {},
			navigateToWorkoutDetails = { },
			navigateUp = {},
			navigateToStudentEdit = {}
		)
	}
}