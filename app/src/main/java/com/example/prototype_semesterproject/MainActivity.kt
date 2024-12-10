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
        var phaseShiftTimer = SystemClock.uptimeMillis()
        var lastTime4 = SystemClock.uptimeMillis()
        var lastTime5 = SystemClock.uptimeMillis()
        var lastTime6 = SystemClock.uptimeMillis()
        var lastTime7 = SystemClock.uptimeMillis()
        var lastTime8 = SystemClock.uptimeMillis()
        var zVal = 5.0
        val timerBlockSpeed = 15L
        val timerBlockSpawn = 1000L
        val timerBlockSpawn2 = 600L
        val timerRTBlockSpawn = 2500L
        val phaseShiftDuration = 3500L
        val phaseShiftDataCooldown = 1000L
        val pushSensing = 5000L
//        val phaseShiftCooldown = 6500L
        val phaseShiftCooldown = 100L
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
        var phaseShift = false
        var z2Arr = mutableListOf<Double>()
        var can_phaseshift = true
        var stop_processing = false
        var phaseshift_cooldown_timer = SystemClock.uptimeMillis()


        override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
            // Set the background frame color
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
            GLES20.glEnable(GLES20.GL_BLEND)
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
            // https://www.learnopengles.com/android-lesson-five-an-introduction-to-blending/
            var randX = Random.nextDouble(-5.0,5.0)
            var randLength = Random.nextDouble(.15,.5)
            var randHeight = Random.nextDouble(0.2,0.9)
            var randColor = Random.nextInt(100,255)
            var newBlock = Square2(randX, lengthVal = randLength, heightVal = randHeight, blockColor = randColor)
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
            var randLength: Double
            var randWidth: Double
            if (_death.value == true) {
                spawn()
                _death.postValue(false)
            }
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            Matrix.setLookAtM(viewMatrix, 0, camX.toFloat(), 0f, 0.5f, camX.toFloat(), 0f, 0f, 0f, 1f, 0f)
            val time = SystemClock.uptimeMillis()
//            val time2 = SystemClock.uptimeMillis()


            if (!can_phaseshift) { // resets ability to phaseshift
                if (time - lastTime6 >= phaseShiftCooldown) {
                    can_phaseshift = true
                    lastTime6 = time
                }
            }
//            println(gravityA.value!!)



            if (time - lastTime2 >= timerBlockSpawn || timerYes2) {
                for (i in 0..2) {
                    var randX = Random.nextDouble(camX-3.0, camX+3.0)
                    var isLengthLong = Random.nextInt(0,100)

                    if (isLengthLong >= 95) {
                        randLength = Random.nextDouble(.45,.5)
                    }
                    else {
                        randLength = Random.nextDouble(.15,.35)
                    }

                    var randHeight = Random.nextDouble(0.15,0.9)
                    var isWidthLong = Random.nextInt(0,100)

                    if (isWidthLong > 100) {
                        randWidth = Random.nextDouble(.25,.4)
                    }
                    else {
                        randWidth = Random.nextDouble(0.125,0.15)
                    }
                    var randColor = Random.nextInt(100,255)
                    var newBlock = Square2(randX, lengthVal = randLength, heightVal = randHeight, widthVal = randWidth, blockColor = randColor)
                    blocksArrTemp.add(newBlock)
                    lastTime2 = time
                    timerYes2 = true
                    blockCoords.add(randX)
                }
            }
            if (time - lastTime7 >= phaseShiftDataCooldown) { // clears the phaseshift data after specific time
                lastTime7 = time
                z2Arr.clear()
            }
            if (time - lastTime3 >= timerBlockSpawn2 || timerYes3) {
                for (i in 0..2) {
                    var randX = Random.nextDouble(camX-6.0, camX+6.0)
                    var isLengthLong = Random.nextInt(0,100)
                    if (isLengthLong >= 95) {
                        randLength = Random.nextDouble(.45,.5)
                    }
                    else {
                        randLength = Random.nextDouble(.15,.35)
                    }
                    var randHeight = Random.nextDouble(0.15,0.9)
                    var isWidthLong = Random.nextInt(0,100)

                    if (isWidthLong > 100) {
                        randWidth = Random.nextDouble(.25,.4)
                    }
                    else {
                        randWidth = Random.nextDouble(0.125,0.15)
                    }
                    var randColor = Random.nextInt(100,255)
                    var newBlock = Square2(randX, lengthVal = randLength, heightVal = randHeight, widthVal = randWidth, blockColor = randColor)
                    blocksArrTemp.add(newBlock)
                    lastTime3 = time
                    timerYes3 = true
                    blockCoords.add(randX)

                }
            }
//            if (phaseShiftTimer)


//            println(zData.value!!)

//            println(gravityC.value)
//            if (gravityB.value!! < 8.5 && gravityC.value!! < -2.5) {
//                println("LOLOL")
//            }



            if (time - lastTime8 >= pushSensing) {
                stop_processing = false
                lastTime8 = time

            }

//            println(zData2.value)
            if (can_phaseshift) {
                if (zData2.value!! >= 3.5 || zData2.value!! <= -3.5) { // Fast pushing motion after a few numbers will be positive, Fast pulling motion after a few numbers will be negative
                println(zData2.value)
                if (zData2.value !in z2Arr){
                    z2Arr.add(zData2.value!!)
//                    println(z2Arr)
                    lastTime7 = SystemClock.uptimeMillis()

                }
                    if (z2Arr.size >= 3) {
                        var positive = false
                        var negative = false
                        for (num in z2Arr) {
                            if (num > 0) {
                                positive = true
                            }
                            if (num < 0) {
                                negative = true
                            }
                        }

                        if (positive && negative) {
                            if (z2Arr.isNotEmpty()) {
                                if ((z2Arr.last() > 0) || (z2Arr.last() < 0)) {
                                    if (!stop_processing) {
//                                        stop_processing = true
                                        println("FORWARD PUSH\n")
                                        z2Arr.clear()
                                        for (block in blocksArr) {
                                            when (block) {
                                                is Square3 -> {
                                                    if (phaseShift == false) {
                                                        block.blockColor = "transparent"
                                                        phaseShift = true
                                                        phaseShiftTimer = SystemClock.uptimeMillis()
                                                    }
                                                }
                                            }

                                        }
                                    }
                                } else if (z2Arr.last() < 0) {
                                    if (!stop_processing) {
//                                        stop_processing = true
                                        println("BACKWARD PUSH\n")
                                        z2Arr.clear()
                                    }
                                }
                            }
                        } else {
                            z2Arr.clear()
                        }
//                    var total_val = 0.0
//                    for (num in z2Arr) {
//                        total_val += num
//                    }
//                    if (total_val < 0) {
//                        println(z2Arr)
//                        println("FORWARD PUSH\n")
//                    }
//                    else if (total_val > 0) {
//                        println(z2Arr)
//                        println("BACKWARDS PUSH\n")
//                    }
//                    z2Arr.clear()
                    }

                }
            }
            if (verticalData.value!! >= 5.5 || verticalData.value!! <= -5.5) {
//                println(verticalData.value)
            }
            if (phaseShift) {
                if (time - phaseShiftTimer >= phaseShiftDuration || timerYes4) { // deactivates phase shift ability
                    phaseShift = false
                    can_phaseshift = false
                    for (block in blocksArr) {
                        when (block) {
                            is Square3 -> {
                                block.blockColor = "black"
                                phaseShiftTimer = time

                            }
                        }
                    }
                }
            }

//            if (time - lastTime5 >= timerRTBlockSpawn) {
//                var randX = Random.nextDouble(-3.0, 3.0)
//                var newRTBlock = Square2(randX, "pink")
//                rotatingBlocksArr.add(newRTBlock)
//                lastTime5 = time
//            }

            if (horizontalData.value!! >= 1.5) { // move left
                camX -= .05
                for (block in blocksArr) {
                    when (block) {
                        is Square3 -> {
                            block.xVal = camX
                        }
                    }
                }
            }
            if (horizontalData.value!! <= -1.5) { // move right
                camX += .05
                for (block in blocksArr) {
                    when (block) {
                        is Square3 -> {
                            block.xVal = camX
                        }
                    }
                }
            }
            println(gravityC.value)
//            if ((verticalData.value!! > 0.0 && verticalData.value!! <= 2.0) || playerJump) {
//            if ((zData.value!! <= -5.0) || playerJump) {
            if ((gravityB.value!! < 10.0 && gravityC.value!! < -.5) || playerJump) {

                if ((camY > 2.5) || playerJumpDown) {
                    camY -= .05
                    playerJumpDown = true
                    if (camY <= 0.0) {
                        camY = 0.0
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

//            if ((zData.value!! >= 7.5)) {
//                for (block in blocksArr) {
//                    when (block) {
//                        is Square3 -> {
//                            if (phaseShift == false) {
//                                block.blockColor = "transparent"
//                                phaseShift = true
//                                phaseShiftTimer = SystemClock.uptimeMillis()
//                            }
//                        }
//                    }
//
//                }
//            }
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
//            println(zData.value)
//            println(verticalData.value)

            val square3 = blocksArr.find { it is Square3 } as Square3
            for (block in blocksArr) {
                when (block) {
                    is Square2 -> {
                        if (square3.blockColor != "transparent") {
                            if (block.zVal <= 0.0) {
                                if (block.xVal != null) {
//                                    if (camX >= (block.xVal!! - .35) && camX <= (block.xVal!! + .35)) {
                                    if (Math.abs(camX - block.xVal!!) <= 0.36) {
                                        if (-0.55+camY <= block.heightVal) {
//                                            var rando = Random.nextInt(20)
////                                    println(rando)
                                            _death.postValue(true)
                                            _deathCounter.postValue(
                                                _deathCounter.value?.plus(1) ?: 1
                                            )
                                        }

                                    }
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


    class Square2(var xVal: Double?= 0.0, var blockColor: Int = 150, var zVal: Double = 5.0, var lengthVal: Double = 0.25, var heightVal: Double = 0.55, var widthVal: Double = 0.15) {

        private var mProgram: Int = 0
        private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}"

        private var vPMatrixHandle: Int = 0
        private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}"

        private val drawOrder = shortArrayOf(
            0, 1, 2, 0, 2, 3,  // front side
            4, 5, 6, 4, 6, 7,  // back side
            0, 1, 5, 0, 5, 4,  // left side
            3, 2, 6, 3, 6, 7,  // right side
            0, 3, 7, 0, 7, 4,  // top side
            1, 2, 6, 1, 6, 5)  // Bottom face
        val COORDS_PER_VERTEX = 3
//        var squareCoords = floatArrayOf(
//            -0.25f,  .55f, 0.0f,     // top left
//            -0.25f, -.55f, 0.0f,     // bottom left
//            0.25f, -.55f, 0.0f,      // bottom right
//            0.25f,  .55f, 0.0f,      // top right
//
//            -0.25f,  .55f, -0.25f,
//            -0.25f, -.55f, -0.25f,
//            0.25f, -.55f, -0.25f,
//            0.25f,  .55f, -0.25f
//        )
        var squareCoords = floatArrayOf(
            (-lengthVal).toFloat(), heightVal.toFloat(), 0.0f,     // top left
            (-lengthVal).toFloat(), -.55f, 0.0f,                   // bottom left
            lengthVal.toFloat(), -.55f, 0.0f,                      // bottom right
            lengthVal.toFloat(), heightVal.toFloat(), 0.0f,        // top right

            (-lengthVal).toFloat(), heightVal.toFloat(), widthVal.toFloat(),
            (-lengthVal).toFloat(), -.55f, widthVal.toFloat(),
            lengthVal.toFloat(), -.55f, widthVal.toFloat(),
            lengthVal.toFloat(), heightVal.toFloat(), widthVal.toFloat()
        )
//        var squareCoords = floatArrayOf(
//            (-lengthVal).toFloat(), .55f, 0.0f,     // top left
//            (-lengthVal).toFloat(), -.55f, 0.0f,                   // bottom left
//            lengthVal.toFloat(), -.55f, 0.0f,                      // bottom right
//            lengthVal.toFloat(), .55f, 0.0f,        // top right
//
//            (-lengthVal).toFloat(), .55f, -0.25f,
//            (-lengthVal).toFloat(), -.55f, -0.25f,
//            lengthVal.toFloat(), -.55f, -0.25f,
//            lengthVal.toFloat(), .55f, -0.25f
//        )
        //heightVal .2 to .9

        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0

        private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
        private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

        init {

            val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram().also {

                // add the vertex shader to program
                GLES20.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES20.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)
            }
        }

        // initialize vertex byte buffer for shape coordinates
        private val vertexBuffer: FloatBuffer =
            // (# of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(squareCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(squareCoords)
                    position(0)
                }
            }

        // initialize byte buffer for the draw list
        private val drawListBuffer: ShortBuffer =
            // (# of coordinate values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }
        fun draw(vPMatrix: FloatArray) {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(mProgram)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

                // Enable a handle to the triangle vertices
                GLES20.glEnableVertexAttribArray(it)

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )
                var blueShade = blockColor.toFloat() / 255f

                // get handle to fragment shader's vColor member
                mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                    // Set color for drawing the triangle
                    var color = floatArrayOf(0.0f, 0.0f, .8f, 1.0f)

                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                }

                // Draw the triangle
                GLES20.glDrawElements(
                    GLES20.GL_TRIANGLES,
                    drawOrder.size,
                    GLES20.GL_UNSIGNED_SHORT,
                    drawListBuffer
                )

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(it)

                // get handle to shape's transformation matrix
                vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

                // Pass the projection and view transformation to the shader
                GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)

                // Draw the triangle
                //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(positionHandle)
            }
        }
        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

    }


    class Square3(var xVal: Double?= 0.0, var blockColor: String = "black", var zVal: Double = 3.0, var yVal: Double = 0.0) {

        private var mProgram: Int = 0
        var color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
        private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}"
        //            "attribute vec4 vPosition;" +
//                "void main() {" +
//                "  gl_Position = vPosition;" +
//                "}"
        private var vPMatrixHandle: Int = 0
        private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}"

        private val drawOrder = shortArrayOf(
            0, 1, 2, 0, 2, 3,  // front side
            4, 5, 6, 4, 6, 7,  // back side
            0, 1, 5, 0, 5, 4,  // left side
            3, 2, 6, 3, 6, 7,  // right side
            0, 3, 7, 0, 7, 4,  // top side
            1, 2, 6, 1, 6, 5)  // Bottom face
        val COORDS_PER_VERTEX = 3
//        var squareCoords = floatArrayOf(
//            -0.15f,  -.2f, 0.0f,     // top left
//            -0.15f, -.55f, 0.0f,     // bottom left
//            0.15f, -.55f, 0.0f,      // bottom right
//            0.15f,  -.2f, 0.0f,      // top right
//
//            -0.15f,  -.2f, -0.25f,
//            -0.15f, -.55f, -0.25f,
//            0.15f, -.55f, -0.25f,
//            0.15f,  -.2f, -0.25f
//        )
        var squareCoords = floatArrayOf(
            -0.10f,  -.35f, 0.0f,     // top left
            -0.10f, -.55f, 0.0f,     // bottom left
            0.10f, -.55f, 0.0f,      // bottom right
            0.10f,  -.35f, 0.0f,      // top right

            -0.10f,  -.35f, -0.1f,
            -0.10f, -.55f, -0.1f,
            0.10f, -.55f, -0.1f,
            0.10f,  -.35f, -0.1f
        )


        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0

        private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
        private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

        init {

            val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram().also {

                // add the vertex shader to program
                GLES20.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES20.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)
            }
        }

        // initialize vertex byte buffer for shape coordinates
        private val vertexBuffer: FloatBuffer =
            // (# of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(squareCoords.size * 4).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(squareCoords)
                    position(0)
                }
            }

        // initialize byte buffer for the draw list
        private val drawListBuffer: ShortBuffer =
            // (# of coordinate values * 2 bytes per short)
            ByteBuffer.allocateDirect(drawOrder.size * 2).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(drawOrder)
                    position(0)
                }
            }
        fun draw(vPMatrix: FloatArray) {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(mProgram)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

                // Enable a handle to the triangle vertices
                GLES20.glEnableVertexAttribArray(it)

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )

                // get handle to fragment shader's vColor member
                mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                    // Set color for drawing the triangle

//                    color = floatArrayOf(0f, 0f, 0f, 1f)
                    if (blockColor == "black") {
                        color = floatArrayOf(0f, 0f, 0f, 1f)
                    }
                    else if (blockColor == "transparent") {
                        color = floatArrayOf(0f, 0f, 0f, .15f)
                    }

                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                }

                // Draw the triangle
                GLES20.glDrawElements(
                    GLES20.GL_TRIANGLES,
                    drawOrder.size,
                    GLES20.GL_UNSIGNED_SHORT,
                    drawListBuffer
                )

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(it)

                // get handle to shape's transformation matrix
                vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

                // Pass the projection and view transformation to the shader
                GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, vPMatrix, 0)

                // Draw the triangle
                //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(positionHandle)
            }
        }
        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

    }


    class Triangle(var xVal: Double?= 0.0, var blockColor: String = "black", var zVal: Double = 2.5) {

        // Set color with red, green, blue and alpha (opacity) values

        private var mProgram: Int = 0

        private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}"

        private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}"

        // number of coordinates per vertex in this array
        val COORDS_PER_VERTEX = 3
        var triangleCoords = floatArrayOf(
            0.0f, 0.05f, 0.3f,
            -0.15f, 0.0f, -0.15f,
            0.15f, 0.0f, -0.15f
        )

        init {

            val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram().also {

                // add the vertex shader to program
                GLES20.glAttachShader(it, vertexShader)

                // add the fragment shader to program
                GLES20.glAttachShader(it, fragmentShader)

                // creates OpenGL ES program executables
                GLES20.glLinkProgram(it)
            }
        }


        private var vertexBuffer: FloatBuffer =
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(triangleCoords)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }
        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }

        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0

        private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
        private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

        fun draw(vPMatrix: FloatArray) {
            // Add program to OpenGL ES environment
            GLES20.glUseProgram(mProgram)

            // get handle to vertex shader's vPosition member
            positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

                // Enable a handle to the triangle vertices
                GLES20.glEnableVertexAttribArray(it)

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
                )

                // get handle to fragment shader's vColor member
                mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                    // Set color for drawing the triangle
                    var color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
                    if (blockColor == "black") {
                        color = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)
                    }
                    GLES20.glUniform4fv(colorHandle, 1, color, 0)
                }

                // Draw the triangle
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(it)
            }
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

val _gravityA = MutableLiveData(0.0)
val gravityA: MutableLiveData<Double> get() = _gravityA
val _gravityB = MutableLiveData(0.0)
val gravityB: MutableLiveData<Double> get() = _gravityB
val _gravityC = MutableLiveData(0.0)
val gravityC: MutableLiveData<Double> get() = _gravityC
class SensorManagerModel(context: Context) : SensorEventListener {
    private var mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mAccelerometer: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var mLinearAcceleration: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private var mGravity: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    var sensor: FloatArray = floatArrayOf()
    var sensor2: FloatArray = floatArrayOf()
    var sensor3: FloatArray = floatArrayOf()
    var sensor4: FloatArray = floatArrayOf()

    val _sensorData = MutableLiveData(sensor)
    val sensorData: MutableLiveData<FloatArray> get() = _sensorData

    val _sensorData2 = MutableLiveData(sensor2)
    val sensorData2: MutableLiveData<FloatArray> get() = _sensorData2

    val _sensorData3 = MutableLiveData(sensor3)
    val sensorData3: MutableLiveData<FloatArray> get() = _sensorData3

    val _sensorData4 = MutableLiveData(sensor4)
    val sensorData4: MutableLiveData<FloatArray> get() = _sensorData4


    init {
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        mGravity= mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)


        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (mLinearAcceleration != null) {
            mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (mGravity != null) {
            mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL)
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
        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            sensorData3.postValue(event.values)
        }
        changeSensorData(event.values, event.sensor.type)
    }

    fun changeSensorData(newSensorData: FloatArray, sensor_type: Int?=Sensor.TYPE_ACCELEROMETER) {
        if (sensor_type == Sensor.TYPE_ACCELEROMETER) {
            if (newSensorData.isNotEmpty()) {
                var horizontal = newSensorData[0]
                var vertical = newSensorData[1]
                var z = newSensorData[2]
                _horizontalData.postValue(horizontal.toDouble())
                _verticalData.postValue(vertical.toDouble())
                _zData.postValue(z.toDouble())
            }
        }
        if (sensor_type == Sensor.TYPE_LINEAR_ACCELERATION) {
            if (newSensorData.isNotEmpty()) {
                var x2 = newSensorData[0]
                var y2 = newSensorData[1]
                var z2 = newSensorData[2]
                _horizontalData2.postValue(x2.toDouble())
                _verticalData2.postValue(y2.toDouble())
                _zData2.postValue(z2.toDouble())
            }
        }
        if (sensor_type == Sensor.TYPE_GRAVITY) {
            if (newSensorData.isNotEmpty()) {
                var x3 = newSensorData[0]
                var y3 = newSensorData[1]
                var z3 = newSensorData[2]
                _gravityA.postValue(x3.toDouble())
                _gravityB.postValue(y3.toDouble())
                _gravityC.postValue(z3.toDouble())
            }
        }
    }

}