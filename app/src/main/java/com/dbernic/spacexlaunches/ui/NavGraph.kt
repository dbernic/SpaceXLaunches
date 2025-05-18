package com.dbernic.spacexlaunches.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dbernic.spacexlaunches.ui.Destinations.DETAILS
import com.dbernic.spacexlaunches.ui.Destinations.LAUNCH_PLACEHOLDER
import com.dbernic.spacexlaunches.ui.Destinations.LIST
import com.dbernic.spacexlaunches.ui.screens.details.DetailsScreen
import com.dbernic.spacexlaunches.ui.screens.list.ListScreen

object Destinations {
    const val LIST = "list"
    const val DETAILS = "details/{launchId}"
    const val LAUNCH_PLACEHOLDER = "launchId"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = LIST) {
        composable(LIST) {
            ListScreen(
                navigateDetails = {
                    navController.navigate(DETAILS.replace("{$LAUNCH_PLACEHOLDER}", it))
                }
            )
        }

        composable(
            DETAILS,
            arguments = listOf(navArgument(LAUNCH_PLACEHOLDER) { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(LAUNCH_PLACEHOLDER)?.let {
                DetailsScreen(it)
            }
        }
    }
}