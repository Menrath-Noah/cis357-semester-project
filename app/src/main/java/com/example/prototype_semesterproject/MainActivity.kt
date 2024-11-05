package com.example.prototype_semesterproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var sensorManagerModel: SensorManagerModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManagerModel = SensorManagerModel(this)
        sensorManagerModel.sensorData.observe(this) { sensorValues ->
            gameViewModel.changeSensorData(sensorValues)


        }
//        enableEdgeToEdge()
        setContent {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Game1024(
                        modifier = Modifier.padding(innerPadding),
                        vm = gameViewModel
                    )
                }

        }
    }
}

suspend fun handleSwipe(scope: PointerInputScope, iAmSwiped: (Swipe) -> Unit) {
    scope.run {
        var totalDragX = 0f
        var totalDragY = 0f
        detectDragGestures(
            onDragStart = {
                // reset total run to zero
                totalDragX = 0f
                totalDragY = 0f
            },
            onDrag = { change, dragAmout ->
                change.consume()
                // Accumulate both x and y distances
                totalDragX += dragAmout.x
                totalDragY += dragAmout.y
            },
            onDragEnd = {
                // Which direction has the larger magnitude?
                val dir =
                    if (totalDragX.absoluteValue > totalDragY.absoluteValue) {
                        // Horizontal swipe
                        if (totalDragX > 0) Swipe.RIGHT else Swipe.LEFT
                    } else {
                        // Vertical swipe
                        if (totalDragY > 0) Swipe.DOWN else Swipe.UP
                    }
                iAmSwiped(dir) // Call the lambda
            },
        )
    }
}


@Composable
fun Game1024(modifier: Modifier = Modifier, vm: GameViewModel) {
    var swipeDirection by remember { mutableStateOf<Swipe?>(null) }
    val cellValues = vm.numbers.observeAsState()
    var gameMessage = vm.gameMessage
    var boardSize = vm.rowLen
    val gameCounter = vm.gameCounter.observeAsState(1)
    var gameOver = vm.gameOver.observeAsState()


    if (gameCounter.value % 4 == 0){
        vm.spawnWall()

    }

    Column(
        modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                handleSwipe(this) {
                    println("Swipe to $it")
                    swipeDirection = it
                    //vm.doSwipe(it)
                }
            })
    {
        Text(
            "",
            fontSize = 22.sp
        )
        NumberGrid(size = boardSize, cells = cellValues.value, modifier=modifier.padding(top = 48.dp)) //.clipToBounds()


        Text(
            "$gameMessage\n",
            fontSize = 26.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { vm.resetGame() }
        ) {
            Text(
                "",
                fontSize = 24.sp,
            )

        }

    }

    if (gameOver.value == true){
        Box(Modifier.background(Color.White).fillMaxSize(), contentAlignment = Alignment.Center){
            Text("GAME OVER",
                fontSize = 36.sp)
        }
    }
}



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NumberGrid(modifier: Modifier = Modifier, size: Int = 4, cells: List<String>?) {
    var color = Color.Red
    val numbersToShow = cells ?:
    (1..19).map { "" }

    FlowRow(maxItemsInEachRow = size, modifier = modifier, horizontalArrangement = Arrangement.spacedBy(0.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
        numbersToShow.forEach {
            if (it == "X"){
                color = Color.Red
            }
            else if (it == "/\\"){
                color = Color.Green
            }
            else{color = Color.White}

            Text(
                "",//it /*.toString()*/,

                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
//                    .border(2.dp, Color.Black)
                    //.wrapContentHeight()
                    .background(color)
                    .padding(10.dp)

            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        Game1024(vm=GameViewModel())

}