package com.example.prototype_semesterproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun mainScreen(
    uid: String,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcometo CubeShake")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            _death.postValue(false)
            navController.navigate("game_config/$uid")

        }) {

            Text("Play Game")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("game_stats/$uid") }) {
            Text("View Stats")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.navigate("login") }) {
            Text("Logout")
        }
    }
}

//@Composable
//fun MainScreen(
//    modifier: Modifier = Modifier,
//    navController: NavController,
//    onLogout: () -> Unit,
//    onStartGame: () -> Unit,
//    onViewStats: () -> Unit
//) {
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(horizontal = 16.dp)
//            .padding(top = 24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Welcome to Cubeshake",
//            fontSize = 30.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        Button(
//            onClick = {navController.popBackStack()},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text("Start Game")
//        }
//
//        Button(
//            //change path to stats screen
//            onClick = {navController.navigate("login")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text("Game Stats")
//        }
//
//        Button(
//            onClick = {navController.navigate("login")},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Text("Log Out")
//        }
//    }
//}
