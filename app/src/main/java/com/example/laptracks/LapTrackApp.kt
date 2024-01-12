package com.example.laptracks

import android.icu.text.CaseMap.Title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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