package com.example.prototype_semesterproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController


val _death = MutableLiveData(false)
val death: LiveData<Boolean> get() = _death
val _deathCounter = MutableLiveData(0)
val deathCounter: LiveData<Int> get() = _deathCounter

@Composable
fun GameConfig(
    sensorManagerModel: SensorManagerModel,
    uid: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    val gLView = MyGLSurfaceView(context)
    val deathState = death.observeAsState(initial = false) // Observe death LiveData
    var routed by rememberSaveable { mutableStateOf(false) }

    if (deathState.value) {
        // Stop sensors and game processes
        // sensorManagerModel//.stopSensors()
        //gLView.releaseResources()

        // Navigate to game_stats
        if (!routed) {
            routed = true
            navController.navigate("game_stats/$uid") {
                popUpTo("game_config/$uid") {
                    inclusive = true
                } // Remove game screen from back stack
            }
        }

        // Reset death state to prevent repeated navigation score
        // Reset death state to prevent repeated navigation
        // _death.value = false
    }

    sensorManagerModel.sensorData.observeForever { sensorValues ->
        sensorManagerModel.changeSensorData(sensorValues)
    }

    AndroidView(factory = { gLView })
}