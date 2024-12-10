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



    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        var randX = Random.nextDouble(-5.0, 5.0)
        var newBlock = Square2(randX)
        gameStartTime = SystemClock.uptimeMillis()
        blocksArr.add(newBlock)
        blockCoords.add(randX)
        blocksArr.add(Square3())
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


        if (time - lastTime2 >= timerBlockSpawn || timerYes2) {
            for (i in 0..2) {
                var randX = Random.nextDouble(camX - 3.0, camX + 3.0)
                var newBlock = Square2(randX)
                blocksArrTemp.add(newBlock)
                lastTime2 = time
                timerYes2 = true
                blockCoords.add(randX)
            }
        }
        if (time - lastTime3 >= timerBlockSpawn2 || timerYes3) {
            for (i in 0..2) {
                var randX = Random.nextDouble(camX - 6.0, camX + 6.0)
                var newBlock = Square2(randX)
                blocksArrTemp.add(newBlock)
                lastTime3 = time
                timerYes3 = true
                blockCoords.add(randX)


            }
        }


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
                                println(rando)
                                _death.postValue(true)
                                _score.postValue((SystemClock.uptimeMillis() - gameStartTime) / 100)
                                _deathCounter.postValue(_deathCounter.value?.plus(1) ?: 1)
                                saveGameStats()
                            }
                        }
                    }

                }

                is Triangle -> {
                    println("Triangle")
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
            where2safe.add(gameStats)
                .addOnSuccessListener {
                    println("Game stats saved successfully.")
                }
                .addOnFailureListener { e ->
                    println("Failed to save game stats: ${e.message}")
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
