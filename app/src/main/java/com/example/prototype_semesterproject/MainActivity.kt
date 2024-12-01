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
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

private val _death = MutableLiveData(false)
val death: LiveData<Boolean> get() = _death

private val _deathCounter = MutableLiveData(0)
val deathCounter: LiveData<Int> get() = _deathCounter

class MainActivity : ComponentActivity() {
    private lateinit var gLView: GLSurfaceView
    private lateinit var sensorManagerModel: SensorManagerModel
    private val _gameMessage = MutableLiveData<String>("")
    val gameMessage: LiveData<String> get() = _gameMessage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManagerModel = SensorManagerModel(this)
        sensorManagerModel.sensorData.observe(this) { sensorValues ->
            sensorManagerModel.changeSensorData(sensorValues)
        }
        gLView = MyGLSurfaceView(this)
        setContentView(gLView)


        enableEdgeToEdge()

}

    class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

        private val renderer: MyGLRenderer


        init {

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2)

            renderer = MyGLRenderer()

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer)

            renderMode = RENDERMODE_CONTINUOUSLY

        }
    }


    class MyGLRenderer : GLSurfaceView.Renderer {
        private lateinit var mSquare: Square2
        private lateinit var mTriangle: Triangle
        private val vPMatrix = FloatArray(16)
        private val projectionMatrix = FloatArray(16)
        private val projectionMatrix2 = FloatArray(16)
        private val viewMatrix = FloatArray(16)
        private val rotationMatrix = FloatArray(16)
        private val rotationMatrix2 = FloatArray(16)

        private val shiftMatrix = FloatArray(16)
        private val shiftMatrix2 = FloatArray(16)
        var lastTime = SystemClock.uptimeMillis()
        var lastTime2 = SystemClock.uptimeMillis()
        var lastTime3 = SystemClock.uptimeMillis()
        var lastTime4 = SystemClock.uptimeMillis()
        var lastTime5 = SystemClock.uptimeMillis()
        var zVal = 5.0
        val timerBlockSpeed = 15L
        val timerBlockSpawn = 1000L
        val timerBlockSpawn2 = 600L
        val timerRTBlockSpawn = 2500L
//        var blocksArr = mutableListOf<Square2>()
        var blocksArr = mutableListOf<Any>()
        var blocksArrTemp = mutableListOf<Any>()
        var rotatingBlocksArr = mutableListOf<Any>()
        var timerYes = false
        var timerYes2 = false
        var timerYes3 = false
        var timerYes4 = false
        var delBlocksArr = mutableListOf<Any>()
        var camX = 0.0
        var camY = 0.0
        var blockCoords = mutableListOf<Double>()
        var tempDelBlockCoords = mutableListOf<Double>()
        var playerJump = false
        var playerJumpDown = false


        override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
            // Set the background frame color
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
            var randX = Random.nextDouble(-5.0,5.0)
            var newBlock = Square2(randX)
            blocksArr.add(newBlock)
            blockCoords.add(randX)
            blocksArr.add(Square3())
//            var randRTX = Random.nextDouble(-5.0,5.0)
//            var newBlockRT = Square2(randRTX, "pink")
//            rotatingBlocksArr.add(newBlockRT)
            //blocksArr.add(Triangle(0.0))
        }

        fun spawn() {
            var randR = Random.nextFloat()
            var randG = Random.nextFloat()
            var randB = Random.nextFloat()
            GLES20.glClearColor(randR, randG, randB, 1.0f)
        }
        @RequiresApi(35)
        override fun onDrawFrame(unused: GL10?) {
            if (_death.value == true) {
                spawn()
                _death.postValue(false)
            }
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            Matrix.setLookAtM(viewMatrix, 0, camX.toFloat(), 0f, 0.5f, camX.toFloat(), 0f, 0f, 0f, 1f, 0f)
            val time = SystemClock.uptimeMillis()
//            val time2 = SystemClock.uptimeMillis()



            if (time - lastTime2 >= timerBlockSpawn || timerYes2) {
                for (i in 0..2) {
                    var randX = Random.nextDouble(camX-3.0, camX+3.0)
                    var newBlock = Square2(randX)
                    blocksArrTemp.add(newBlock)
                    lastTime2 = time
                    timerYes2 = true
                    blockCoords.add(randX)
                }
            }
            if (time - lastTime3 >= timerBlockSpawn2 || timerYes3) {
                for (i in 0..2) {
                    var randX = Random.nextDouble(camX-6.0, camX+6.0)
                    var newBlock = Square2(randX)
                    blocksArrTemp.add(newBlock)
                    lastTime3 = time
                    timerYes3 = true
                    blockCoords.add(randX)



                }
            }
//            if (time - lastTime5 >= timerRTBlockSpawn) {
//                var randX = Random.nextDouble(-3.0, 3.0)
//                var newRTBlock = Square2(randX, "pink")
//                rotatingBlocksArr.add(newRTBlock)
//                lastTime5 = time
//            }

            if (horizontalData.value!! >= 2) {
                camX -= .05
                for (block in blocksArr) {
                    when (block) {
                        is Square3 -> {
                            block.xVal = camX
                        }
                    }


                }
            }
            if (horizontalData.value!! <= -2) {
                camX += .05
                for (block in blocksArr) {
                    when (block) {
                        is Square3 -> {
                            block.xVal = camX
                        }
                    }
                }
            }
            if ((verticalData.value!! > 0.0 && verticalData.value!! <= 2.0) || playerJump) {

                if ((camY > 3.5) || playerJumpDown) {
                    camY -= .05
                    playerJumpDown = true
                    if (camY <= 0.0) {
                        playerJumpDown = false
                        playerJump = false
                    }
                }
                else {
                    camY += .05
                    for (block in blocksArr) {
                        when (block) {
                            is Square3 -> {
                                block.yVal = camY
                            }
                        }
                    }
                    playerJump = true
                }
            }

//            if (zData2.value!! >= 7.5 || zData2.value!! <= -7.5) { // Fast pushing motion after a few numbers will be positive, Fast pulling motion after a few numbers will be negative
//                println(zData2.value)
//            }
            if (verticalData2.value!! >= 5.5 || verticalData2.value!! <= -5.5) {
                println(verticalData2.value)
            }



            /* ROTATING MATRIX - IN PROGRESS
            if (rotatingBlocksArr.isNotEmpty()) {
                for (rtBlock in rotatingBlocksArr) {

                    if (time - lastTime4 >= timerBlockSpeed || timerYes4) {
                        println("HHEHEHEE")
                        rtBlock.zVal -= .05
                        lastTime4 = time
                        timerYes4 = true
                    }
                    if (rtBlock.zVal <= 0) {
                        println("ZVAL: ${rtBlock.zVal}")
                        delBlocksArr.add(rtBlock)
                    }

                    Matrix.setIdentityM(shiftMatrix, 0)
                    val scratch = FloatArray(16)

                    // Create a rotation transformation for the triangle
                    val timeWow = SystemClock.uptimeMillis() % 4000L
                    val angle = 0.090f * timeWow.toInt()
                    Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

                    // Combine the rotation matrix with the projection and camera view
                    // Note that the vPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.

                    rtBlock.xVal?.let { Matrix.translateM(shiftMatrix, 0, it.toFloat(), .25f, -rtBlock.zVal.toFloat()) }
                    Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, shiftMatrix, 0)
                    Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

                    // Draw triangle
                    rtBlock.draw(scratch)
                    }
            }
             */

            for (block in blocksArr) {
                when (block) {
                    is Square2 -> {
                        if (time - lastTime >= timerBlockSpeed || timerYes) {
                            //println("HHEHEHEE")
                            block.zVal -= .05
                            lastTime = time
                            timerYes = true
                        }


                        if (block.zVal <= 0) {
                            //println("ZVAL: ${block.zVal}")
                            delBlocksArr.add(block)
                            block.xVal?.let { tempDelBlockCoords.add(it) }
                        }

                        Matrix.setIdentityM(shiftMatrix, 0)
                        block.xVal?.let {
                            Matrix.translateM(
                                shiftMatrix,
                                0,
                                it.toFloat(),
                                0.0f,
                                -block.zVal.toFloat()
                            )
                        }
                        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
                        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, shiftMatrix, 0)

                        block.draw(vPMatrix)
                    }
                    is Triangle -> {
                        //println("YEEEE")
                        Matrix.setIdentityM(shiftMatrix, 0)
                        Matrix.translateM(shiftMatrix, 0, 0.0f, 0.0f, -0.5f)

                        // Calculate the projection and view transformation
                        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, shiftMatrix, 0)

                        // Draw shape
                        block.draw(vPMatrix)
                    }
                }

            }
            for (block in blocksArr) {
                when (block) {
                    is Square3 -> {
//                        block.xVal = camX
                        Matrix.setIdentityM(shiftMatrix, 0)
                        block.xVal?.let {
                            Matrix.translateM(
                                shiftMatrix,
                                0,
                                camX.toFloat(),
                                camY.toFloat(),
                                0.05f
                            )
                        }
                        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
                        Matrix.multiplyMM(vPMatrix, 0, vPMatrix, 0, shiftMatrix, 0)

                        block.draw(vPMatrix)
                    }
                }
            }

            for (block in blocksArr) {
                when (block) {
                    is Square2 -> {
                        if (block.zVal <= 0) {
                            if (block.xVal != null) {
                                if (camX >= (block.xVal!! - .45) && camX <= (block.xVal!! + .45)) {
                                    var rando = Random.nextInt(20)
//                                    println(rando)
                                    _death.postValue(true)
                                    _deathCounter.postValue(_deathCounter.value?.plus(1) ?: 1)

                                }
                            }
                        }

                    }
                    is Triangle -> {println("Triangle")}
                }
            }



            blocksArr.addAll(blocksArrTemp)

            blocksArrTemp.clear()

            blocksArr.removeAll(delBlocksArr)
            blockCoords.removeAll(tempDelBlockCoords)
            tempDelBlockCoords.clear()

            delBlocksArr.clear()
            timerYes = false
            timerYes2 = false
            timerYes3 = false
            timerYes4 = false
        }

        override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            GLES20.glViewport(0, 0, width, height)

            val ratio: Float = width.toFloat() / height.toFloat()

            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 0.5f, 35.0f)

        }

    }


}

val _horizontalData = MutableLiveData(0.0)
val horizontalData: MutableLiveData<Double> get() = _horizontalData
val _verticalData = MutableLiveData(0.0)
val verticalData: MutableLiveData<Double> get() = _verticalData
val _zData = MutableLiveData(0.0)
val zData: MutableLiveData<Double> get() = _zData

val _horizontalData2 = MutableLiveData(0.0)
val horizontalData2: MutableLiveData<Double> get() = _horizontalData2
val _verticalData2 = MutableLiveData(0.0)
val verticalData2: MutableLiveData<Double> get() = _verticalData2
val _zData2 = MutableLiveData(0.0)
val zData2: MutableLiveData<Double> get() = _zData2

class SensorManagerModel(context: Context) : SensorEventListener {
    private var mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mAccelerometer: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var mLinearAcceleration: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    var sensor: FloatArray = floatArrayOf()
    var sensor2: FloatArray = floatArrayOf()

    val _sensorData = MutableLiveData(sensor)
    val sensorData: MutableLiveData<FloatArray> get() = _sensorData

    val _sensorData2 = MutableLiveData(sensor2)
    val sensorData2: MutableLiveData<FloatArray> get() = _sensorData2


    init {
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (mLinearAcceleration != null) {
            mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            sensorData.postValue(event.values)
        }
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            sensorData2.postValue(event.values)
        }
        changeSensorData(event.values, event.sensor.type)
    }
    
    fun changeSensorData(newSensorData: FloatArray, sensor_type: Int?=Sensor.TYPE_ACCELEROMETER) {
        if (newSensorData.isNotEmpty()) {
            var horizontal = newSensorData[0]
            var vertical = newSensorData[1]
            var z = newSensorData[2]
            _horizontalData.postValue(horizontal.toDouble())
            _verticalData.postValue(vertical.toDouble())
            _zData.postValue(z.toDouble())
        }
        if (newSensorData.isNotEmpty()) {
            var x2 = newSensorData[0]
            var y2 = newSensorData[1]
            var z2 = newSensorData[2]
            _horizontalData2.postValue(x2.toDouble())
            _verticalData2.postValue(y2.toDouble())
            _zData2.postValue(z2.toDouble())
        }
    }

}