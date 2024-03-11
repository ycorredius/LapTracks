package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetails
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentEditViewModel
import kotlinx.coroutines.launch

object StudentEditDestination : NavigationDestination {
	override val route = "student_edit"
	override val titleRes = R.string.student_edit
	const val studentIdArg = "studentId"
	val routeWithArg = "$route/{$studentIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentEditScreen(
	viewModel: StudentEditViewModel = hiltViewModel(),
	navigateUp: () -> Unit,
	openAndPop: (String, String) -> Unit
) {
	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(id = StudentEditDestination.titleRes),
				canNavigateBack = true,
				navigateUp = navigateUp
			)
		}
	) { innerPadding ->
		val coroutine = rememberCoroutineScope()

		Column(
			modifier = Modifier
				.padding(innerPadding)
		) {
			val teams = viewModel.teams.collectAsStateWithLifecycle(initialValue = emptyList())

			StudentEditBody(
				student = viewModel.uiState.studentDetails,
				isError = viewModel.uiState.isStudentValid,
				onChange = viewModel::updateUiState,
				teams = teams.value,
				onUpdate = {
					coroutine.launch {
						viewModel.updateStudent()
						if (viewModel.uiState.isStudentValid) {
							openAndPop(
								"${StudentDetailsDestination.route}/${viewModel.uiState.studentDetails.id}",
								StudentEditDestination.routeWithArg
							)
						}
					}
				}
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentEditBody(
	student: StudentDetails,
	onChange: (StudentDetails) -> Unit,
	onUpdate: (StudentDetails) -> Unit,
	isError: Boolean,
	teams: List<Team>
) {
	var selectedTeam by remember { mutableStateOf(Pair("Team name", "Team Id")) }
	var isExpanded by remember { mutableStateOf(false) }
	if (teams.isNotEmpty()) {
		val studentTeam = teams.filter { it.id == student.teamId }
		selectedTeam = Pair(studentTeam.first().name, studentTeam.first().id)
	}
	Column(
		verticalArrangement = Arrangement.spacedBy(14.dp),
		modifier = Modifier
			.fillMaxHeight()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {

		OutlinedTextField(
			value = student.firstName,
			onValueChange = { onChange(student.copy(firstName = it)) },
			label = { Text(stringResource(R.string.first_name)) },
			isError = isError && student.firstName.isEmpty(),
			supportingText = {
				if (student.firstName.isEmpty() && isError) {
					Text(
						text = "First Name Can't be Blank",
						color = MaterialTheme.colorScheme.error
					)
				}
			},
			modifier = Modifier.fillMaxWidth(),
			enabled = true,
			singleLine = true
		)
		OutlinedTextField(
			value = student.lastName,
			onValueChange = { onChange(student.copy(lastName = it)) },
			label = { Text(stringResource(R.string.last_name)) },
			isError = isError && student.lastName.isEmpty(),
			supportingText = {
				if (student.lastName.isEmpty() && isError) {
					Text(text = "Last Name Can't be Blank", color = MaterialTheme.colorScheme.error)
				}
			},
			modifier = Modifier.fillMaxWidth(),
			enabled = true,
			singleLine = true
		)
		OutlinedTextField(
			value = student.displayName,
			onValueChange = { onChange(student.copy(displayName = it)) },
			label = { Text(stringResource(R.string.display_name)) },
			isError = student.displayName.isEmpty() && isError,
			supportingText = {
				if (student.displayName.isEmpty() && isError) {
					Text(
						text = "Display Name Can't be Blank",
						color = MaterialTheme.colorScheme.error
					)
				}
			},
			modifier = Modifier.fillMaxWidth(),
			enabled = true,
			singleLine = true
		)
		ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
			OutlinedTextField(
				value = selectedTeam.first,
				onValueChange = {  },
				readOnly = true,
				label = { Text(text = "Team") },
				supportingText = {
					if (student.teamId.isBlank() && isError) {
						Text(
							text = "Please select a team",
							color = MaterialTheme.colorScheme.error
						)
					}
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
							onChange(student.copy(teamId = team.id))
							isExpanded = !isExpanded
						}
					)
				}
			}
		}
		Button(onClick = { onUpdate(student) }, modifier = Modifier.fillMaxWidth(0.5f)) {
			Text(text = "Update")
		}
	}
}

@Preview
@Composable
fun StudentEditPreview() {
	StudentEditBody(
		student = StudentDetails(
			firstName = "Bill", lastName = "Smith", displayName = "BSmith"
		),
		isError = false,
		onChange = {/* */ },
		teams = emptyList(),
		onUpdate = {}
	)
}