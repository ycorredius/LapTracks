package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamCreateViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamDetails
import kotlinx.coroutines.launch

object TeamCreateDestination : NavigationDestination {
    override val route = "teamCreate"
    override val titleRes = R.string.team_create
}

@Composable
fun TeamCreateScreen(
    teamCreateViewModel: TeamCreateViewModel = hiltViewModel(),
    navigateToTeams: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    Scaffold() {
        Column(modifier = Modifier.padding(it)) {
            TeamCreateBody(
                teamDetails = teamCreateViewModel.teamUiState.teamDetails,
                updateTeamDetails = teamCreateViewModel::updateUiState,
                saveTeam = {
                    coroutine.launch {
                        teamCreateViewModel.saveTeam()
                        navigateToTeams()
                    }
                }
            )
        }
    }
}

@Composable
fun TeamCreateBody(
    teamDetails: TeamDetails,
    updateTeamDetails: (TeamDetails) -> Unit ={},
    saveTeam:()->Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        OutlinedTextField(
            value = teamDetails.name,
            onValueChange = { updateTeamDetails(teamDetails.copy(name = it)) },
            label = { Text("Team Name")},
            singleLine = true,
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { saveTeam() }, modifier = Modifier
            .padding(0.dp, 10.dp)
            .fillMaxWidth()) {
            Text(text = "Create Team")
        }
    }
}

@Preview
@Composable
fun TeamCreatePreview(){
    LapTracksTheme {
        TeamCreateBody(teamDetails = TeamDetails(), updateTeamDetails = {}, saveTeam = {})
    }
}