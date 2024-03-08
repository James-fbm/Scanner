package com.example.scanner.ui.component

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scanner.ui.component.collection.CollectionMain
import com.example.scanner.ui.component.home.HomeMain
import com.example.scanner.ui.component.project.ProjectMain
import com.example.scanner.ui.viewmodel.CollectionViewModel
import com.example.scanner.ui.viewmodel.ProjectViewModel

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
            val projectId = backStackEntry.arguments?.getString("projectId")?.toIntOrNull() ?: 0
            val projectViewModel: ProjectViewModel = hiltViewModel()

            // passing projectId to the injection constructor of the viewModel is tedious
            // currently we explicitly pass projectId as a mutable variable to the viewModel
            // maybe reimplemented if there is a better method
            projectViewModel.setProjectId(projectId)

            ProjectMain(
                projectViewModel = projectViewModel,
                onCollectionItemClicked = {collectionId ->
                    navController.navigate("collection/${collectionId}")
                }
            )
        }

        composable("collection/{collectionId}") {backStackEntry ->
            val collectionId = backStackEntry.arguments?.getString("collectionId")?.toIntOrNull() ?: 0
            val collectionViewModel: CollectionViewModel = hiltViewModel()

            collectionViewModel.setCollectionId(collectionId)
            CollectionMain(
                collectionViewModel = collectionViewModel,
                onVolumeItemClicked = {_ -> }
            )
        }
    }
}