package com.shaunyarbrough.laptracks.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.convertLongToString
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.getLastLapTimeString
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

object ParticipantSummaryDestination : NavigationDestination {
	override val route = "participant_summary"
	override val titleRes = R.string.practice_summary
}

@Composable
fun PracticeSummaryBody(
	currentDate: String,
	interval: String,
	participants: Map<Student, List<Long>>,
	setParticipantTime: (Student, Long) -> Unit,
	totalTime: Long,
	isEnabled: Boolean,
	isTimerRunning: Boolean,
	updateTotalTime: (Long) -> Unit,
	onStartClick: () -> Unit,
	onFinishClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(0.dp, 10.dp),
		verticalArrangement = Arrangement.SpaceBetween
	) {
		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "$currentDate - Practice ",
					textAlign = TextAlign.Center,
					fontSize = 18.sp
				)
				Text(text = "$interval meters", fontSize = 18.sp)
			}
			Spacer(modifier = Modifier.padding(10.dp))
			ParticipantSummaryLazyColumn(
				participants = participants,
				setParticipantTime = { participant, time ->
					setParticipantTime(participant, time)
				},
				totalTime,
				isEnabled = isEnabled
			)
		}
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(text = convertLongToString(totalTime), fontSize = 30.sp)
			Row(
				modifier = Modifier.fillMaxWidth(),
			) {
				Button(
					onClick = {
						onStartClick()
						if (isTimerRunning) updateTotalTime(totalTime)
					},
					modifier = Modifier.weight(0.3f)
				) {
					if (isTimerRunning) {
						Text(text = "Pause")
					} else {
						Text(
							text = stringResource(id = R.string.start),
							fontSize = dimensionResource(id = R.dimen.button_font).value.sp
						)
					}
				}
				Spacer(modifier = Modifier.weight(0.1f))
				Button(
					onClick = { onFinishClick() },
					enabled = !isEnabled,
					modifier = Modifier.weight(0.3f)
				) {
					Text(
						text = stringResource(id = R.string.finish),
						fontSize = dimensionResource(id = R.dimen.button_font).value.sp
					)
				}
			}
		}
	}
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeSummaryScreen(
	workoutViewModel: WorkoutViewModel,
	navigateUp: () -> Unit,
	onFinishClick: () -> Unit
) {
	val workoutUiState by workoutViewModel.workoutUiState.collectAsState()

	val date = SimpleDateFormat("dd-MM-yyy")
	val currentDate = date.format(Date())

	var totalTime by remember { mutableLongStateOf(0L) }
	var isTimerRunning by remember { mutableStateOf(false) }
	var isEnabled by remember { mutableStateOf(isTimerRunning) }
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
	LaunchedEffect(key1 = isTimerRunning, key2 = totalTime) {
		if (isTimerRunning) {
			delay(1)
			totalTime += 1000L
		}
	}

	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(ParticipantSummaryDestination.titleRes),
				canNavigateBack = true,
				scrollBehavior = scrollBehavior,
				navigateUp = navigateUp
			)
		},
		modifier = Modifier.padding(30.dp, 0.dp)
	) { innerPadding ->
		Box(modifier = Modifier.padding(innerPadding)) {
			PracticeSummaryBody(
				currentDate = currentDate,
				interval = workoutUiState.interval,
				participants = workoutUiState.participantsList,
				totalTime = totalTime,
				isEnabled = isEnabled,
				setParticipantTime = { participant, time ->
					workoutViewModel.setParticipantTime(participant, time)
				},
				isTimerRunning = isTimerRunning,
				onStartClick = {
					isTimerRunning = !isTimerRunning
					isEnabled = !isEnabled
				},
				onFinishClick = onFinishClick,
				updateTotalTime = { workoutViewModel.updateTotalTime(it) }
			)
		}
	}
}

@Composable
private fun ParticipantSummaryLazyColumn(
	participants: Map<Student, List<Long>>,
	setParticipantTime: (Student, Long) -> Unit,
	totalTime: Long,
	isEnabled: Boolean
) {
	Column {
		participants.forEach { participant ->
			Card(
				modifier = Modifier
					.padding(0.dp, 3.dp)
					.clickable {
						if (isEnabled) setParticipantTime(participant.key, totalTime)
					},
				colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
				shape = MaterialTheme.shapes.extraSmall
			) {
				Row(
					horizontalArrangement = Arrangement.SpaceAround,
					modifier = Modifier
						.padding(0.dp, 10.dp)
						.fillMaxWidth()
				) {
					Text(
						text = participant.key.displayName,
						style = MaterialTheme.typography.titleLarge
					)
					Column {
						Text(text = "Laps")
						Text(text = "${participant.value.size}")
					}
					Column {
						Text(text = "Last lap")
						Text(text = getLastLapTimeString(participant.value))
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun PracticeSummaryScreenPreview() {
	LapTracksTheme {
		PracticeSummaryBody(
			currentDate = "1234",
			interval = "400",
			isTimerRunning = false,
			isEnabled = false,
			onStartClick = {},
			participants = mapOf(
				Student(
					firstName = "Billy",
					lastName = "Smith",
					displayName = "BSmith",
				) to listOf(5_000L)
			),
			setParticipantTime = { _, _ -> },
			totalTime = 5_000L,
			onFinishClick = {},
			updateTotalTime = {}
		)
	}
}