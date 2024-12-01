package com.example.prototype_semesterproject

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import androidx.annotation.RequiresApi
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


    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        var randX = Random.nextDouble(-5.0, 5.0)
        var newBlock = Square2(randX)
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
                                _deathCounter.postValue(_deathCounter.value?.plus(1) ?: 1)

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

}
