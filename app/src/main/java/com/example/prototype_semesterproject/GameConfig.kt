package com.example.prototype_semesterproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView


val _death = MutableLiveData(false)
val death: LiveData<Boolean> get() = _death
val _deathCounter = MutableLiveData(0)
val deathCounter: LiveData<Int> get() = _deathCounter

@Composable
fun GameConfig(
    gameMessage: LiveData<String>,
    sensorManagerModel: SensorManagerModel,
    uid: String
) {
    val context = LocalContext.current
    val gLView = MyGLSurfaceView(context)

    sensorManagerModel.sensorData.observeForever { sensorValues ->
        sensorManagerModel.changeSensorData(sensorValues)
    }

    AndroidView(factory = { gLView })
}
