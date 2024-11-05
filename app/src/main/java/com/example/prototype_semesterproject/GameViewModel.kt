package com.example.prototype_semesterproject

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Runnable
import kotlin.random.Random


class GameViewModel: ViewModel() {
    private val _numbers = MutableLiveData<List<String>>()
    val numbers: LiveData<List<String>> get() = _numbers
    var gameMessage = ""
    var gameOn = false
    var rowLen = 4 // default set to 4 (len 4 row)
    var endingIndex = 19 // default set to 15 (4x4)
    var playerIndex = 17
    var playerTimer = 0

    private val _gameCounter = MutableLiveData(1)
    val gameCounter: MutableLiveData<Int> get() = _gameCounter

    val _horizontalData = MutableLiveData(0.0)
    val horizontalData: MutableLiveData<Double> get() = _horizontalData

    val _gameOver = MutableLiveData(false)
    val gameOver: MutableLiveData<Boolean> get() = _gameOver

    init {
        _numbers.value = (1..endingIndex + 1).toList().map { "" }
    }

    fun changeSensorData(newSensorData: FloatArray) {
        if (newSensorData.isNotEmpty()) {
            var horizontal = newSensorData[0]
            println(horizontal)
            _horizontalData.postValue(horizontal.toDouble())
        }
    }

    fun resetGame() {
        gameOn = false
        _numbers.value = (1..endingIndex + 1).toList().map { "" }
        _gameCounter.postValue(1)
        initiateGame()

    }


    fun initiateGame() {
            gameOn = true
            var numArr = _numbers.value?.toMutableList()
            numArr?.set(playerIndex, "/\\")
            var blockPlacer = Random.nextInt(0, 4)
            numArr?.set(blockPlacer, "X")
            _numbers.value = numArr!!
            val mainHandler = Handler(Looper.getMainLooper())

            mainHandler.postDelayed(object : Runnable {
                override fun run() {
                    if (gameOn and gameOver.value!! == false) {
                        gameLoop()
                        _gameCounter.postValue(_gameCounter.value?.plus(1) ?: 0)
                        mainHandler.postDelayed(this, 350)
                    }
                }
            }, 350)


    }


    fun spawnWall() {
        _gameCounter.postValue(1)
        var numArr = _numbers.value?.toMutableList()
//        numArr?.set(21, "/\\")
        var blockPlacer = Random.nextInt(0, 4)
        numArr?.set(blockPlacer, "X")
        _numbers.value = numArr!!
        println("_____---__--__")
        println(horizontalData)
        //_numbers.value = numArr!!
    }

    fun movePlayer() {
        //if (playerTimer % 2 == 0) {
            var numArr = _numbers.value?.toMutableList()
            if (horizontalData.value!! >= 1) {
                if (playerIndex != 16) {
                    numArr?.set(playerIndex - 1, "/\\")
                    numArr?.set(playerIndex, "")
                    playerIndex -= 1
                }

            }

            if (horizontalData.value!! <= -1) {
                if (playerIndex != 19) {
                    numArr?.set(playerIndex + 1, "/\\")
                    numArr?.set(playerIndex, "")
                    playerIndex += 1
                }

            }
            _numbers.value = numArr!!
            playerTimer += 1
        }

    //}


    fun gameLoop() {
        println("GAME LOOP RUNNING")
        var indexArr = mutableListOf<Int>()
        val numArr = _numbers.value?.toMutableList()
        for (i in 0..19) {
            //delay(100)
            if (numArr?.get(i) == "X") {
                if (i >= 16) {
                    if (numArr?.get(i) == "X") {
                        numArr?.set(i, "")
                    }
                } else if (i !in indexArr) {
                    var indexNum = i
                    indexNum += rowLen
                    indexArr.add(indexNum)
                    if (numArr?.get(indexNum) == "") {
                        var currentVal = "X"
                        numArr?.set(indexNum, currentVal)
                        numArr?.set(indexNum - rowLen, "")
                    }
                    else if (numArr?.get(indexNum) == "/\\") {
                        println("ENDING GAME")
                        endGame()
                    }
                }
            }
        }

        _numbers.value = numArr!!
        movePlayer()
    }

    fun endGame(){
        gameOver.postValue(true)
    }

}


//}
//// https://kotlinlang.org/docs/cancellation-and-timeouts.html#run-non-cancellable-block
//// https://kotlinlang.org/docs/coroutines-basics.html#coroutines-are-light-weight

