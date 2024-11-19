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


        setContent {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    overView()

                    AndroidView(
                        factory = { MyGLSurfaceView(this@MainActivity) }
                    )
                }
            }

        }


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

    @Composable
    fun overView(){
        val deathState by death.observeAsState()
        val deathCount by deathCounter.observeAsState()
//        if (deathState == true) {
            Row(horizontalArrangement = Arrangement.Center) {

                    Text("$deathCount", color = Color.Black, fontSize = 32.sp)
            }
//        }
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
        var blockCoords = mutableListOf<Double>()
        var tempDelBlockCoords = mutableListOf<Double>()


        override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
            // Set the background frame color
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
            var randX = Random.nextDouble(-5.0,5.0)
            var newBlock = Square2(randX)
            blocksArr.add(newBlock)
            blockCoords.add(randX)
//            var randRTX = Random.nextDouble(-5.0,5.0)
//            var newBlockRT = Square2(randRTX, "pink")
//            rotatingBlocksArr.add(newBlockRT)
            //blocksArr.add(Triangle(0.0))
        }

        @RequiresApi(35)
        override fun onDrawFrame(unused: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            Matrix.setLookAtM(viewMatrix, 0, camX.toFloat(), 0f, 0.5f, camX.toFloat(), 0f, 0f, 0f, 1f, 0f)
            val time = SystemClock.uptimeMillis()
//            val time2 = SystemClock.uptimeMillis()



//            if (time - lastTime2 >= timerBlockSpawn || timerYes2) {
//                for (i in 0..2) {
//                    var randX = Random.nextDouble(-3.0, 3.0)
//                    var newBlock = Square2(randX)
//                    blocksArrTemp.add(newBlock)
//                    lastTime2 = time
//                    timerYes2 = true
//                    blockCoords.add(randX)
//                }
//            }
            if (time - lastTime3 >= timerBlockSpawn2 || timerYes3) {
                for (i in 0..2) {
                    var randX = Random.nextDouble(-6.0, 6.0)
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
            }
            if (horizontalData.value!! <= -2) {
                camX += .05
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




//            println(blocksArr.size)
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
                    is Square2 -> {
                        if (block.zVal <= 0) {
                            if (block.xVal != null) {
                                if (camX >= block.xVal!! - .35 && camX <= block.xVal!! + .35) {
                                    var rando = Random.nextInt(20)
                                    println(rando)
                                    _death.postValue(true)
                                    _deathCounter.postValue(_deathCounter.value?.plus(1) ?: 1)


                                }
                            }
                        }

                    }
                    is Triangle -> {println("Triangle")}
                }
            }
//            for (coord in blockCoords) {
//                if (camX >= coord - .25 && camX <= coord + .25) {
//                    println("HIIIII")
//                }
//            }



            blocksArr.addAll(blocksArrTemp)
//
            blocksArrTemp.clear()
//            if (delBlocksArr.size > 1) {
            blocksArr.removeAll(delBlocksArr)
            blockCoords.removeAll(tempDelBlockCoords)
            tempDelBlockCoords.clear()
//            }
            delBlocksArr.clear()
            timerYes = false
            timerYes2 = false
            timerYes3 = false
            timerYes4 = false
        }

        override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
            GLES20.glViewport(0, 0, width, height)
            //println(width)
            //println(height)

            val ratio: Float = width.toFloat() / height.toFloat()

            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method

            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 0.5f, 35.0f)

        }

    }


    class Square2(var xVal: Double?= 0.0, var blockColor: String = "blue", var zVal: Double = 5.0) {

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
        var squareCoords = floatArrayOf(
            -0.25f,  .55f, 0.0f,     // top left
            -0.25f, -.55f, 0.0f,     // bottom left
            0.25f, -.55f, 0.0f,      // bottom right
            0.25f,  .55f, 0.0f,      // top right

            -0.25f,  .55f, -0.25f,
            -0.25f, -.55f, -0.25f,
            0.25f, -.55f, -0.25f,
            0.25f,  .55f, -0.25f
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
                    var color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)
                    if (blockColor == "blue") {
                        color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)
                    }
                    else if (blockColor == "pink") {
                        color = floatArrayOf(1.0f, 0.0f, 0.5f, 1.0f)
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
//                GLES20.glDrawElements(
//                    GLES20.GL_TRIANGLES,
//                    drawOrder.size,
//                    GLES20.GL_UNSIGNED_SHORT,
//                    drawListBuffer
//                )

                // Disable vertex array
                GLES20.glDisableVertexAttribArray(it)
            }
        }
    }

    
}

val _horizontalData = MutableLiveData(0.0)
val horizontalData: MutableLiveData<Double> get() = _horizontalData
class SensorManagerModel(context: Context) : SensorEventListener {
    private var mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mAccelerometer: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    var sensor: FloatArray = floatArrayOf()

    val _sensorData = MutableLiveData(sensor)
    val sensorData: MutableLiveData<FloatArray> get() = _sensorData



    init {
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        sensorData.postValue(event.values)
    }
    fun changeSensorData(newSensorData: FloatArray) {
        if (newSensorData.isNotEmpty()) {
            var horizontal = newSensorData[0]
            //println(horizontal)
            _horizontalData.postValue(horizontal.toDouble())
        }
    }

}