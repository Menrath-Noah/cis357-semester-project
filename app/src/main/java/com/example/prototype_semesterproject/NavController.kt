package com.example.prototype_semesterproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    gameMessage: LiveData<String>,
    sensorManagerModel: SensorManagerModel,
    vm: MyGLRenderer
) {
    NavHost(
        navController = navController,
        startDestination = "login",//"game_config/{uid}",
        modifier = modifier
    ) {
        composable("login") {
            Login(
                navController = navController,
                onLoginSuccess = {
                    // Navigate to game_config with the UID as an argument
//                    navController.navigate("game_config/$it")
//                    navController.navigate("game_stats/$it")
                    navController.navigate("main_screen/$it")
                }
            )
        }

        composable("signup") {
            CreateAccount(
                navController = navController
            )
        }

        composable("game_config/{uid}") {
            it.arguments?.getString("uid")?.let { uid ->
                GameConfig(
                    sensorManagerModel = sensorManagerModel,
                    uid = uid,
                    navController = navController
                )
            }
        }

        composable("game_stats/{uid}") {
            it.arguments?.getString("uid")?.let { uid ->
                GameStatsScreen(
                    //gameMessage = gameMessage,
                    //sensorManagerModel = sensorManagerModel,
                    vm = vm,
                    userId = uid,
                    navController = navController
                )
            }
        }
        // Main Screen Route
        composable("main_screen/{uid}") {
            it.arguments?.getString("uid")?.let { uid ->
                mainScreen(
                    uid = uid,
                    navController = navController
                )
            }
        }

    }
}