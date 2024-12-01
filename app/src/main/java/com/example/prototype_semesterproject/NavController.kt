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

//@Composable
//fun NavHostScreen(navController: NavHostController, modifier: Modifier = Modifier) {
  //  Column(
    //    modifier = Modifier.fillMaxSize().padding(16.dp),
      //  horizontalAlignment = Alignment.CenterHorizontally
   // ) {
     //   NavHost(navController,startDestination = "login") {
       //     composable("login") {
         //       Login(modifier = modifier, navController) {
           //         navController.navigate("game/$it")
             //   }
           // }
            //composable("game/{uid}") {
              //  it.arguments?.getString("uid")?.let { uid ->
                    //inser Gamescreen
                //}
            //}
//            composable("settings") {
//                Settings(
//                    vm = vm,
//                    navController = navController
//                )
//            }

//            composable("stats/{uid}") {
//                it.arguments?.getString("uid")?.let { uid ->
//                    GameStatisticsScreen(
//                        vm = vm,
//                        uid = uid,
//                        navController = navController
//                    )
//                }
//            }
/*
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    gameMessage: LiveData<String>,
    sensorManagerModel: SensorManagerModel,
) {
    //val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {


        composable("login") {
            Login(
                modifier = Modifier,
                navController,
                onLoginSuccess ,
                navController.navigate("game_config/$it"))
        }
        composable("game_config/{uid}") {
            GameConfig(
                gameMessage = gameMessage,
                sensorManagerModel = sensorManagerModel
            )
        }
        composable("signup") {
            CreateAccount(
                navController = navController
            )
        }
    }
}
*/
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    gameMessage: LiveData<String>,
    sensorManagerModel: SensorManagerModel
) {
    NavHost(
        navController = navController,
        startDestination = "game_config/{uid}",
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
