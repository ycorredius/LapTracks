package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamUiState
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamsViewModel

object TeamsDestination : NavigationDestination {
	override val route = "teams"
	override val titleRes = R.string.teams
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
	teamsViewModel: TeamsViewModel = hiltViewModel(),
	navigateToTeamEntry: () -> Unit,
	navigateToTeam: (String) -> Unit,
) {
	Scaffold(
		topBar = {
			LapTrackAppTopAppBar(
				title = stringResource(id = TeamsDestination.titleRes),
				canNavigateBack = false,
			)
		},
		floatingActionButton = {
			FloatingActionButton(onClick = { navigateToTeamEntry() }) {
				Icon(imageVector = Icons.Default.Add, contentDescription = "Add team button")
			}
		}
	) { padding ->
		val teamUiState = teamsViewModel.teamUiState
		Box(
			modifier = Modifier
				.padding(padding)
				.fillMaxSize()
		) {
			when (teamUiState) {
				is TeamUiState.Success -> TeamsBody(
					teams = teamUiState.teams,
					navigateToTeam = navigateToTeam
				)

				is TeamUiState.Loading -> LoadingScreen()
				is TeamUiState.Error -> ErrorScreen()
			}
		}
	}
}

@Composable
fun TeamsBody(
	teams: List<Team>,
	navigateToTeam: (String) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(10.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (teams.isEmpty()) {
			Text(
				text = stringResource(id = R.string.no_teams),
				textAlign = TextAlign.Center,
				fontSize = 24.sp,
			)
		} else {
			LazyColumn(
				modifier = Modifier.fillMaxSize(),
				verticalArrangement = Arrangement.spacedBy(5.dp)
			) {
				items(teams) {
					TeamItem(
						team = it,
						navigateToTeam
					)
				}
			}
		}
	}
}

@Composable
fun TeamItem(
	team: Team,
	navigateToTeam: (String) -> Unit
) {
	Card(elevation = CardDefaults.cardElevation(2.dp),
		shape = MaterialTheme.shapes.extraSmall,
		modifier = Modifier.clickable { navigateToTeam(team.id) }
	) {
		Row(
			modifier = Modifier
				.padding(10.dp)
				.fillMaxWidth()
		) {
			Text(
				text = team.name,
				fontSize = 20.sp
			)
		}
	}
}

@Preview
@Composable
fun EmptyTeamsScreenPreview() {
	LapTracksTheme {
		TeamsBody(teams = emptyList(),
			navigateToTeam = {/* nothing */ })
	}
}

@Composable
fun LoadingScreen() {
	Column(
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		CircularProgressIndicator(
			modifier = Modifier.width(100.dp),
			color = MaterialTheme.colorScheme.primary,
			trackColor = MaterialTheme.colorScheme.surfaceVariant,
		)
	}
}

@Composable
private fun ErrorScreen() {
	Text(text = "Something went wrong!!!")
}

@Preview
@Composable
fun TeamsScreenPreview() {
	LapTracksTheme {
		TeamsBody(teams = listOf(Team(id = "some string", name = "Best Team", userId = "someUUid")),
			navigateToTeam = { /*nothing*/ })
	}
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
	LapTracksTheme {
		LoadingScreen()
	}
}