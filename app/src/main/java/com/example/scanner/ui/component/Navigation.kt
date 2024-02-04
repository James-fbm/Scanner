package com.example.scanner.ui.component

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scanner.ui.component.home.HomeMain
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scanner.ui.component.camera.ExternalDirPreview
import com.example.scanner.ui.component.camera.CameraMain
import com.example.scanner.ui.component.project.ProjectMain

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeMain(hiltViewModel()) {projectId ->
                navController.navigate("project/${projectId}")
            }
        }

        composable("project/{projectId}") {backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")?.toInt()
            ProjectMain(hiltViewModel())
        }
    }
}