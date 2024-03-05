package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Team
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamsViewModel

object TeamsDestination : NavigationDestination {
    override val route = "teams"
    override val titleRes = R.string.teams
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    teamsViewModel: TeamsViewModel = hiltViewModel(),
    navigateToTeamEntry: () -> Unit
) {
    val teams by teamsViewModel.teams.collectAsState(initial = emptyList())
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TeamsBody(teams = teams)
        }
    }
}

@Composable
fun TeamsBody(
    teams: List<Team>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(teams) {
                    TeamItem(team = it)
                }
            }
        }
    }
}

@Composable
fun TeamItem(
    team: Team
) {
    Card(modifier = Modifier.clickable { /* TODO */ }) {
        Text(text = team.name)
    }
}

@Preview
@Composable
fun EmptyTeamsScreenPreview() {
    LapTracksTheme {
        TeamsBody(teams = emptyList())
    }
}

@Preview
@Composable
fun TeamsScreenPreview() {
    LapTracksTheme {
        TeamsBody(teams = listOf(Team(id = "somestring", name = "Best Team", userId = "someUUid")))
    }
}