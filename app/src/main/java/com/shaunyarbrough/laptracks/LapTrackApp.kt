package com.shaunyarbrough.laptracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shaunyarbrough.laptracks.ui.BottomBarRoutes

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController())
= remember(navController){
	AppState(navController = navController)
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
fun BottomBarRow(
	navController: NavHostController,
) {
	val tabList = listOf(
		BottomBarRoutes.TEAM,
		BottomBarRoutes.WORKOUT,
	)
	val navStackBackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navStackBackEntry?.destination

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceAround,
		verticalAlignment = Alignment.CenterVertically
	) {
		tabList.forEach { tab ->
			BottomBarItems(
				tab = tab,
				currentDestination = currentDestination,
				navController = navController
			)
		}
	}
}

@Composable
fun BottomBarItems(
	tab: BottomBarRoutes,
	currentDestination: NavDestination?,
	navController: NavHostController
) {
	val selected = currentDestination?.hierarchy?.any { it.route == tab.routes } == true
	val contentColor =
		if (selected) {
			Color.Unspecified
		} else MaterialTheme.colorScheme.onPrimary

	IconButton(
		onClick = { navController.navigate(tab.routes) },
		enabled = !selected
	) {
		Icon(
			painter = painterResource(id = tab.icon),
			contentDescription = tab.routes,
			tint = contentColor
		)
	}
}

@Stable
class AppState(
	val navController: NavHostController
) {
	private val routes = BottomBarRoutes.entries.map { it.routes }

	val shouldBottomBarShow: Boolean
		@Composable get() =
			navController.currentBackStackEntryAsState().value?.destination?.route in routes
}