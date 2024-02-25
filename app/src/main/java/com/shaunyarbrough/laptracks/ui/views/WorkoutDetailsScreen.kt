package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.convertLongToString
import com.shaunyarbrough.laptracks.getFastestLap
import com.shaunyarbrough.laptracks.getLapTimeAverage
import com.shaunyarbrough.laptracks.getSlowestLap
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentDetails
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutDetailViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.WorkoutDetails

object WorkoutDetailsDestination : NavigationDestination {
	override val route = "workout_details"
	override val titleRes = R.string.workout_details
	const val workoutIdArgs = "workoutId"
	val routeWithArgs = "$route/{$workoutIdArgs}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsScreen(
	viewModel: WorkoutDetailViewModel = hiltViewModel(),
	navigateUp: () -> Unit
) {
	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(id = WorkoutDetailsDestination.titleRes),
				canNavigateBack = true,
				navigateUp = navigateUp
			)
		}
	) { innerPadding ->
		val workoutDetailsUiState = viewModel.workoutUiState.collectAsState()
		val workoutDetails = workoutDetailsUiState.value.workoutDetails
		val studentDetails = workoutDetailsUiState.value.studentDetails

		Column(modifier = Modifier.padding(innerPadding)) {
			WorkoutDetailsBody(
				workoutDetails,
				studentDetails
			)
		}
	}
}

@Composable
fun WorkoutDetailsBody(
	workoutDetails: WorkoutDetails,
	studentDetails: StudentDetails
) {
	val averageLapTime: String = getLapTimeAverage(workoutDetails.lapList)
	Column(modifier = Modifier
		.fillMaxSize()
		.padding(10.dp)) {
		Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
			Text(text = "${studentDetails.firstName} ", style = MaterialTheme.typography.titleLarge, fontSize = 26.sp)
			Text(text = studentDetails.lastName, style = MaterialTheme.typography.titleLarge, fontSize = 26.sp)
		}
		Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
			.fillMaxWidth()
			.padding()) {
			Text(text = workoutDetails.date, style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
			Text(text = "${workoutDetails.interval} meters",style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
		}
		Divider(modifier = Modifier.padding(10.dp))
		LazyVerticalGrid(
			columns = GridCells.Adaptive(minSize = 150.dp),
			verticalArrangement = Arrangement.spacedBy(3.dp),
			horizontalArrangement = Arrangement.spacedBy(3.dp)
		) {
			item {
				Card(
					shape = MaterialTheme.shapes.extraSmall,
					colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
					modifier = Modifier
						.fillMaxSize()
						.size(125.dp),

				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
						Text(text = "Total Time", style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp, textAlign = TextAlign.Center)
						Text(text = convertLongToString(workoutDetails.totalTime), style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
					}
				}
			}
			item {
				Card(
					shape = MaterialTheme.shapes.extraSmall,
					colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
					modifier = Modifier.size(125.dp)
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
						Text(
							text = "Average lap time",
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 20.sp
						)
						Text(
							text = getLapTimeAverage(workoutDetails.lapList),
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 20.sp
						)
					}
				}
			}
			item {
				Card(
					shape = MaterialTheme.shapes.extraSmall,
					colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
					modifier = Modifier.size(125.dp)
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
						Text(
							text = "Fastest Lap",
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 20.sp
						)
						Text(
							text = getFastestLap(workoutDetails.lapList),
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 20.sp
						)
					}
				}
			}
			item {
				Card(
					shape = MaterialTheme.shapes.extraSmall,
					colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
					modifier = Modifier
						.size(125.dp)
						.fillMaxSize()
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
						Text(
							text = "Slowest lap",
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 20.sp
						)
						Text(
							text = getSlowestLap(workoutDetails.lapList),
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 20.sp
						)
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun WorkoutDetailsPreview() {
	LapTracksTheme {
		WorkoutDetailsBody(
			workoutDetails = WorkoutDetails(),
			studentDetails = StudentDetails()
		)
	}
}