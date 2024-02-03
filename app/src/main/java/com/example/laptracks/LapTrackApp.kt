package com.example.laptracks

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.laptracks.ui.navigation.AppNavHost
import com.example.laptracks.ui.views.ParticipantDestination
import com.example.laptracks.ui.views.StudentListDestination


@Composable
fun LapTrackApp(navController: NavHostController = rememberNavController()){
  AppNavHost(navController = navController)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LapTrackAppTopAppBar(
  title: String,
  canNavigateBack: Boolean,
  modifier: Modifier = Modifier,
  scrollBehavior: TopAppBarScrollBehavior? = null,
  navigateUp: () -> Unit = {}
) {
  CenterAlignedTopAppBar(
    title = { Text(title) },
    modifier = modifier,
    scrollBehavior = scrollBehavior,
    navigationIcon = {
      if (canNavigateBack) {
        IconButton(onClick = navigateUp) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button)
          )
        }
      }
    }
  )
}

@Composable
fun LapTrackAppBottomAppBar(
  navController: NavHostController
){
  BottomAppBar(
    actions = {
      IconButton(onClick = { navController.navigate(ParticipantDestination.route) }, Modifier.weight(0.3f)) {
        Icon(imageVector = Icons.Filled.Home, contentDescription = "Home button")
      }
      Spacer(modifier = Modifier.weight(0.1f))
      IconButton(onClick = { navController.navigate(StudentListDestination.route) }, Modifier.weight(0.3f)) {
        Icon(imageVector = Icons.Filled.Person , contentDescription = "Students")
      }
    },
  )
}