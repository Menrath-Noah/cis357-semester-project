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
    sensorManagerModel: SensorManagerModel
) {
    NavHost(
        navController = navController,
        startDestination = "login",//"game_config/{uid}",
        modifier = modifier
    ) {
        composable("login") {
            Login(
                navController = navController,
                onLoginSuccess = { uid ->
                    // Navigate to game_config with the UID as an argument
                    navController.navigate("game_config/$uid")
                }
            )
        }

        composable("signup") {
            CreateAccount(
                    navController = navController
            )
        }

        composable("game_config/{uid}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            GameConfig(
                gameMessage = gameMessage,
                sensorManagerModel = sensorManagerModel
            )
        }
    }
}
