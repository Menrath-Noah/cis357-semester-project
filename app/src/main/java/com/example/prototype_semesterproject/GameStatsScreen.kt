package com.example.prototype_semesterproject

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

//score
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameStatsScreen(userId: String, vm:MyGLRenderer, navController:NavController) {
    val gameStatsList by vm.gameStatsList.observeAsState(emptyList())

    // Load data
    LaunchedEffect(Unit) {
        vm.loadGameStats()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Statistics", fontSize = 30.sp, style = MaterialTheme.typography.titleLarge)

        Text(
            text = "User ID: $userId",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(25.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { vm.sortByDate() }) {
                Text("Sort by Date")
            }
            Button(onClick = { vm.sortByScore() }) {
                Text("Sort by Score")
            }
            Button(
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Text("Back")
            }
        }
//
        Spacer(modifier = Modifier.height(16.dp))

        if (gameStatsList.isEmpty()) {
            Text(
                text = "No game statistics available.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            LazyColumn {
                items(gameStatsList) { gameStat ->
                    GameStatsItem(gameStat)
                }
            }
        }
    }
}


@Composable
fun GameStatsItem(gameStat: MyGLRenderer.GameStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Score: ${gameStat.score}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Played: ${gameStat.date.toDate()}",
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
