package com.example.prototype_semesterproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

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
    var lastTime9 = SystemClock.uptimeMillis()
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
    val statSaveCooldown= 20000L

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
    var gameStartTime = 0L
    val _score = MutableLiveData<Long>(0)
    val score: LiveData<Long> get() = _score
    private val auth = FirebaseAuth.getInstance()
    private val _gameStatsList = MutableLiveData<List<GameStats>>()
    val gameStatsList: LiveData<List<GameStats>> get() = _gameStatsList
    private var isDateAscending = true
    private var isScoreAscending = true
    var _loading = MutableLiveData<Boolean>(true)
    val loading: LiveData<Boolean> get() = _loading
    var _lastScore = MutableLiveData<Int>(-1)
    val lastScore: MutableLiveData<Int> get() = _lastScore
    val time2 = SystemClock.uptimeMillis()
    var lastScore2 = -1
    // private val _death = MutableLiveData(false)
    //private val deathState = _death.observeAsState(initial = false)
    //private val _deathCounter = MutableLiveData(0)
    //val deathCounter: LiveData<Int> get() = _deathCounter


    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        // https://www.learnopengles.com/android-lesson-five-an-introduction-to-blending/
        var randX = Random.nextDouble(-5.0, 5.0)
        var randLength = Random.nextDouble(.15, .5)
        var randHeight = Random.nextDouble(0.2, 0.9)
        var randColor = Random.nextInt(100, 255)
        var newBlock =
            Square2(randX, lengthVal = randLength, heightVal = randHeight, blockColor = randColor)
        blocksArr.add(newBlock)
        blockCoords.add(randX)
        blocksArr.add(Square3())
        gameStartTime = SystemClock.uptimeMillis()
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
            isDateAscending = false
            isScoreAscending = false
            saveGameStats()
            //   _death.postValue(false)
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.setLookAtM(
            viewMatrix,
            0,
            camX.toFloat(),
            0f,
            0.5f,
            camX.toFloat(),
            0f,
            0f,
            0f,
            1f,
            0f
        )
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
                var randX = Random.nextDouble(camX - 3.0, camX + 3.0)
                var isLengthLong = Random.nextInt(0, 100)

                if (isLengthLong >= 95) {
                    randLength = Random.nextDouble(.45, .5)
                } else {
                    randLength = Random.nextDouble(.15, .35)
                }

                var randHeight = Random.nextDouble(0.15, 0.9)
                var isWidthLong = Random.nextInt(0, 100)

                if (isWidthLong > 100) {
                    randWidth = Random.nextDouble(.25, .4)
                } else {
                    randWidth = Random.nextDouble(0.125, 0.15)
                }
                var randColor = Random.nextInt(100, 255)
                var newBlock = Square2(
                    randX,
                    lengthVal = randLength,
                    heightVal = randHeight,
                    widthVal = randWidth,
                    blockColor = randColor
                )
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
                var randX = Random.nextDouble(camX - 6.0, camX + 6.0)
                var isLengthLong = Random.nextInt(0, 100)
                if (isLengthLong >= 95) {
                    randLength = Random.nextDouble(.45, .5)
                } else {
                    randLength = Random.nextDouble(.15, .35)
                }
                var randHeight = Random.nextDouble(0.15, 0.9)
                var isWidthLong = Random.nextInt(0, 100)

                if (isWidthLong > 100) {
                    randWidth = Random.nextDouble(.25, .4)
                } else {
                    randWidth = Random.nextDouble(0.125, 0.15)
                }
                var randColor = Random.nextInt(100, 255)
                var newBlock = Square2(
                    randX,
                    lengthVal = randLength,
                    heightVal = randHeight,
                    widthVal = randWidth,
                    blockColor = randColor
                )
                blocksArrTemp.add(newBlock)
                lastTime3 = time
                timerYes3 = true
                blockCoords.add(randX)

            }
        }
//            if (phaseShiftTimer)


//            println(zData0.value!!)

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
//                println(zData2.value)
                if (zData2.value !in z2Arr) {
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
//                                    println("FORWARD PUSH\n")
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
//                                    println("BACKWARD PUSH\n")
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
        if (verticalData0.value!! >= 5.5 || verticalData0.value!! <= -5.5) {
//                println(verticalData0.value)
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

        if (horizontalData0.value!! >= 1.5) { // move left
            camX -= .05
            for (block in blocksArr) {
                when (block) {
                    is Square3 -> {
                        block.xVal = camX
                    }
                }
            }
        }
        if (horizontalData0.value!! <= -1.5) { // move right
            camX += .05
            for (block in blocksArr) {
                when (block) {
                    is Square3 -> {
                        block.xVal = camX
                    }
                }
            }
        }
//        println(gravityC.value)
//            if ((verticalData0.value!! > 0.0 && verticalData0.value!! <= 2.0) || playerJump) {
//            if ((zData0.value!! <= -5.0) || playerJump) {
        if ((gravityB.value!! < 10.0 && gravityC.value!! < -3.5) || playerJump) {

            if ((camY > 2.5) || playerJumpDown) {
                camY -= .05
                playerJumpDown = true
                if (camY <= 0.0) {
                    camY = 0.0
                    playerJumpDown = false
                    playerJump = false
                }
            } else {
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

//            if ((zData0.value!! >= 7.5)) {
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
//            println(zData0.value)
//            println(verticalData0.value)

        if (death.value == false) {
            val square3 = blocksArr.find { it is Square3 } as Square3
            for (block in blocksArr) {
                when (block) {
                    is Square2 -> {
                        if (square3.blockColor != "transparent") {
                            if (block.zVal <= 0.0) {
                                if (block.xVal != null) {
//                                    if (camX >= (block.xVal!! - .35) && camX <= (block.xVal!! + .35)) {
                                    if (Math.abs(camX - block.xVal!!) <= 0.36) {
                                        if (-0.55 + camY <= block.heightVal) {
//                                            var rando = Random.nextInt(20)
//                                            println(rando)
                                            _death.postValue(true)
                                            val currentTime = SystemClock.uptimeMillis()
                                            _score.postValue((currentTime - gameStartTime) / 100)
                                            _deathCounter.postValue(_deathCounter.value?.plus(1) ?: 1)
                                            break
                                        }

                                    }
                                }
                            }
                        }

                    }

                    is Triangle -> {
                        println("Triangle")
                    }
                }
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

    data class GameStats(
        var score: Long = 0,
        val date: Timestamp = Timestamp.now()
    )

    private fun saveGameStats() {

            val gameStats = GameStats(
                score = score.value!!,
                date = Timestamp.now()
            )

            val userId = auth.currentUser?.uid
            if (userId != null) {
                val myDB = Firebase.firestore
                val where2safe = myDB.collection("players").document(userId).collection("gamestats")

                if (score.value?.toInt() != lastScore2) {
                    println("-----")
                    println(score.value?.toInt())
                    println(lastScore.value)
                    println("-----")
                    _lastScore.postValue(score.value!!.toInt())
                    lastScore2 = score.value!!.toInt()
//                    _lastScore.value = score.value!!.toInt()
                    where2safe.add(gameStats)
                        .addOnSuccessListener {
                            println("Game stats saved successfully.")

//
                        }
                        .addOnFailureListener { e ->
                            println("Failed to save game stats: ${e.message}")
                        }
                }
            } else {
                    println("User ID is null, cannot save game stats.")
                }
        }


    fun loadGameStats() {
        val myDB = Firebase.firestore
        val userId = auth.currentUser?.uid
        _gameStatsList.value = emptyList()
        _loading.value = true
        if (userId != null) {
            myDB.collection("players")
                .document(userId)
                .collection("gamestats")
                .get()
                .addOnSuccessListener { documents ->
                    documents.forEach { x ->
                        try {
                            val gameStats = x.toObject(GameStats::class.java)
                            if (gameStats != null) {
                                val currentList = _gameStatsList.value ?: emptyList()
                                _gameStatsList.value = currentList + gameStats
                            }
                        } catch (e: Exception) {
                            println("Error converting document: ${x.id}, ${e.message}")
                        }
                        _loading.value = false
                    }
                }
                .addOnFailureListener { e ->
                    println("Error loading game stats: ${e.message}")
                }
        }
    }

    fun sortByDate() {
        _gameStatsList.value = if (isDateAscending) {
            _gameStatsList.value?.sortedBy { it.date }
        } else {
            _gameStatsList.value?.sortedByDescending { it.date }
        }
        isDateAscending = !isDateAscending
    }

    fun sortByScore() {
        _gameStatsList.value = if (isScoreAscending) {
            _gameStatsList.value?.sortedBy { it.score }
        } else {
            _gameStatsList.value?.sortedByDescending { it.score }
        }
        isScoreAscending = !isScoreAscending
    }
}