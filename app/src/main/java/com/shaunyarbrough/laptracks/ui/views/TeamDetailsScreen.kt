package com.shaunyarbrough.laptracks.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shaunyarbrough.laptracks.LapTrackAppTopAppBar
import com.shaunyarbrough.laptracks.R
import com.shaunyarbrough.laptracks.data.Student
import com.shaunyarbrough.laptracks.ui.navigation.NavigationDestination
import com.shaunyarbrough.laptracks.ui.theme.LapTracksTheme
import com.shaunyarbrough.laptracks.ui.viewmodels.StudentUiModel
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamDetailsViewModel
import com.shaunyarbrough.laptracks.ui.viewmodels.TeamWithStudentsDetails

object TeamsDetailsDestination : NavigationDestination {
    override val titleRes = R.string.team_details
    override val route: String = "team_details"
    const val teamIdArgs = "teamId"
    val routeWithArgs = "$route/{$teamIdArgs}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsDetailsScreen(
    viewModel: TeamDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToStudentEntry: () -> Unit,
    navigateToStudentDetails: (String) -> Unit
) {
    val team: TeamWithStudentsDetails by viewModel.team.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            LapTrackAppTopAppBar(
                title = stringResource(R.string.team_details, team.name),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToStudentEntry() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Student")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding))
        TeamDetailsBody(
            team = team,
            navigateToStudentDetails = navigateToStudentDetails
        )
    }
}

@Composable
fun TeamDetailsBody(
    team: TeamWithStudentsDetails,
    navigateToStudentDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(text = "Students")
        Divider(modifier = Modifier.padding(0.dp, 10.dp))
        if (team.students.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_students),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
            )
        } else {
            LazyColumn {
                items(team.students) {
                    TeamStudentItem(
                        student = it,
                        navigateToStudentDetails
                    )
                }
            }
        }
    }
}

@Composable
fun TeamStudentItem(
    student: Student?,
    navigateToStudentDetails: (String) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.extraSmall,
        modifier = Modifier.clickable { student?.id?.let { navigateToStudentDetails(it) } }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = " ${student?.firstName} ${student?.lastName}",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
fun TeamDetailsPreview() {
    LapTracksTheme {
        TeamDetailsBody(
            team = TeamWithStudentsDetails(
                name = "Very best team",
                students = listOf(Student("sometime", "billy", "smith", "bsmith", teamId = "team"))
            ),
            navigateToStudentDetails = {}
        )
    }
}
