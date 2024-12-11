# Cubeshake
---
by Dennis Eisele, Noah Menrath & Stephen Robinson

## Overview
---
Cubeshake is adapted from the imfamous game cubefield with a fresh twist! Instead of using keybooard input on a pc, Cubeshake enables mobile android users to shake there phone to get past obstacles. If you would like to make a similar application please follow our code snipppets and captions below to supplement your current understanding of mobile app development in Android Studio .

## 1. Getting Started with Environment Setup
---
### project build.gradle.kts:
```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

}
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}
```

### app build.gradle.kts:
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

}

android {
    namespace = "com.example.prototype_semesterproject"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.prototype_semesterproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:31.0.0"))
    //implementation("com.google.firebase:firebase-auth-ktx")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}
apply(plugin = "com.google.gms.google-services")
```
---
### AndroidManifest.xml:
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.OpenGLAttempt"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.OpenGLAttempt">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```
## 2. Basic App
---
Instantiating  sensor manager, opengl and vm in MainActivity:
```kotlin
class MainActivity : ComponentActivity() {
    private lateinit var sensorManagerModel: SensorManagerModel
    private lateinit var vm: MyGLRenderer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        sensorManagerModel = SensorManagerModel(this)
        vm = MyGLRenderer()
        setContent {
            AppNavHost(
                sensorManagerModel = sensorManagerModel,
                vm = vm
            )
        }
    }

}
```
## 3. Implementing Sensors and Models

#### Motion Sensor modules:

To help you understand how to utilize Android's motion sensors, click these links to get started:

[click here](https://developer.android.com/reference/kotlin/android/hardware/SensorManager)

[click here](https://developer.android.com/develop/sensors-and-location/sensors/sensors_motion#kotlin)

```kotlin
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorManagerModel(context: Context) : SensorEventListener {
    private var mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var mAccelerometer: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var mLinearAcceleration: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private var mGravity: Sensor? = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    var sensor: FloatArray = floatArrayOf()

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
```
> There is a sensor event listener that is keeping track of all the differnet phone motion sensing data. Within the init, we are registering listeners of specific motion sensing types in order to utilize different motion controls.

In order to use the SensorManagerModel class, you need to use the built in 'onSensorChanged' function:
```kotlin
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

```

> When the event listeners that we registered have new data,'onSensorChanged' will be called. We check the type of event listener that was triggered and we save the motion data to an array.
---
After the new sensor data has been added to their respective arrays, 'changeSensorData' (a custom function we built) is called:
```kotlin
fun changeSensorData(newSensorData: FloatArray, sensor_type: Int?=Sensor.TYPE_ACCELEROMETER) {
        if (sensor_type == Sensor.TYPE_ACCELEROMETER) {
            if (newSensorData.isNotEmpty()) {
                var horizontal = newSensorData[0]
                var vertical = newSensorData[1]
                var z = newSensorData[2]
                _horizontalData0.postValue(horizontal.toDouble())
                _verticalData0.postValue(vertical.toDouble())
                _zData0.postValue(z.toDouble())
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

```
> 'changeSensorData' handles updating individual motion sensing variables per motion event type.

## 4. Implementing Open Graphics Library 


#### OpenGL ES modules:

To get started on working with Opengl ES, open this link to understand the basics of how shapes are created and rendered:

https://developer.android.com/develop/ui/views/graphics/opengl

```kotlin
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
```
#### MyGLSurfaceView:
Create the MyGLSurfaceView which initializes the MyGLRenderer class:
```kotlin
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
```
---
#### MyGLRenderer class:
onSurfaceCreated will create your GL environment:
```kotlin
class MyGLRenderer : GLSurfaceView.Renderer {
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(220F, 180F, 255F, 1.0f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    }

    @RequiresApi(35)
    override fun onDrawFrame(unused: GL10?) {
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
    }
```
> 'onDrawFrame' will constantly update the game frames. This contains your game logic. You can see in the code above, it uses the motion sensor data to control player movement.
```kotlin
    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 0.5f, 35.0f)

    }
}
```
## 5. Handling Data in FireStore
```kotlin
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
```
>'saveGameStats' function takes the player scores and then connects to FireStore to save the player score stats in the database.
---
```kotlin

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
                    _gameStatsList.value = _gameStatsList.value?.sortedByDescending { it.date }
                }
                .addOnFailureListener { e ->
                    println("Error loading game stats: ${e.message}")
                }
        }
    }
```
> 'loadGameStats' function is called when the player wants to view their statistics. It calls the current player's game data from FireStore and loads the data into a list. It is automatically sorted by date.

## 6. Collision Detection

```kotlin
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

                    }
                }
            }
        }
```

---
## Conclusion 
Thank you for completing our Cubeshake app tutorial! We hope you found this guide informative and helpful in understanding how to develop and deploy a mobile app using Android Studio and Firebase. 

If you need further assistance or have questions about any part of the tutorial, please explore our GitHub repository [link](https://github.com/Menrath-Noah/cis357-semester-project/tree/main), watch our [video walkthroughs]( https://youtu.be/yRsFyAJK6PE), or contact us at:

robinss3@mail.gvsu.edu
eiseled@mail.gvsu.edu
menrathn@mail.gvsu.edu


We're always here to help! Keep developing and good luck with Cubeshake!

