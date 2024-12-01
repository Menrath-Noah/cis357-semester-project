package com.example.prototype_semesterproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.Matrix
import android.os.SystemClock
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.random.Random

//private val _death = MutableLiveData(false)
//val death: LiveData<Boolean> get() = _death

//private val _deathCounter = MutableLiveData(0)
//val deathCounter: LiveData<Int> get() = _deathCounter

class MainActivity : ComponentActivity() {
    private lateinit var sensorManagerModel: SensorManagerModel
    private val _gameMessage = MutableLiveData<String>("")
    val gameMessage: LiveData<String> get() = _gameMessage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManagerModel = SensorManagerModel(this)

        setContent {
            AppNavHost(
                gameMessage = gameMessage,
                sensorManagerModel = sensorManagerModel
            )
        }
    }

}



val _horizontalData = MutableLiveData(0.0)
val horizontalData: MutableLiveData<Double> get() = _horizontalData
val _verticalData = MutableLiveData(0.0)
val verticalData: MutableLiveData<Double> get() = _verticalData
val _zData = MutableLiveData(0.0)
val zData: MutableLiveData<Double> get() = _zData
