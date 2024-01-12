//package com.example.laptracks.ui.views
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.laptracks.R
//import com.example.laptracks.ui.AppViewModeProvider
//import com.example.laptracks.ui.navigation.NavigationDestination
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.nestedscroll.nestedScroll
//import androidx.compose.ui.res.stringResource
//import com.example.laptracks.LapTrackAppTopAppBar
//import com.example.laptracks.data.DataSource
//import com.example.laptracks.ui.viewmodels.StudentViewModel
//
//object StudentDestination : NavigationDestination {
//  override val route = "student"
//  override val titleRes = R.string.students
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun StudentScreen(
//  modifier: Modifier = Modifier,
//  viewModel: StudentViewModel = viewModel(factory = AppViewModeProvider.Factory)
//){
//
//  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
////  val uiState = viewModel.uiState.collectAsState()
//  Scaffold(
//    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//    topBar = {
//      LapTrackAppTopAppBar(
//        title = stringResource(StudentDestination.titleRes),
//        canNavigateBack = false,
//        scrollBehavior = scrollBehavior
//      )
//    }
//  ) {
//    innerPadding ->
//    ParticipantScreen(
//      students = DataSource.students,
//      participants = DataSource.students,
//      modifier = Modifier.padding(innerPadding)
//    )
//  }
//}