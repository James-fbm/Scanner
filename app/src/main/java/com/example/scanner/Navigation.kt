package com.example.scanner

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scanner.ui.component.home.HomeMain
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navArgument
import com.example.scanner.ui.component.start.StartMain

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeMain(hiltViewModel()) {projectId ->
                navController.navigate("start/${projectId}")
            }
        }

        composable("start/{projectId}") {backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toInt()
            StartMain(projectId)
        }
    }
}