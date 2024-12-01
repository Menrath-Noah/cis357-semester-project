package com.example.prototype_semesterproject

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavHost(navController,startDestination = "login") {
            composable("login") {
                Login(modifier = modifier, navController) {
                    navController.navigate("game/$it")
                }
            }
            composable("game/{uid}") {
                it.arguments?.getString("uid")?.let { uid ->
                    //inser Gamescreen 
                }
            }
//            composable("settings") {
//                Settings(
//                    vm = vm,
//                    navController = navController
//                )
//            }
            composable("signup") {
                CreateAccount(
                    navController = navController
                )
            }
//            composable("stats/{uid}") {
//                it.arguments?.getString("uid")?.let { uid ->
//                    GameStatisticsScreen(
//                        vm = vm,
//                        uid = uid,
//                        navController = navController
//                    )
//                }
//            }
        }
    }
}
@Composable
fun AppNavHost(
    gameMessage: LiveData<String>,
    sensorManagerModel: SensorManagerModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "game_config") {
        composable("game_config") {
            GameConfig(
                gameMessage = gameMessage,
                sensorManagerModel = sensorManagerModel
            )
        }
    }
}

