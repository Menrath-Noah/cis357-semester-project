# cis357-semester-project

---

> "Noting here that i have access to the repository and the invite was valid" - Stephen 10:00 pm EDT

#### Motion Sensor modules:
```kotlin
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
```
---
#### OpenGL ES modules:
```kotlin
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
```

To help you understand how to utilize Android's motion sensors, click these links to get started:
---
https://developer.android.com/reference/kotlin/android/hardware/SensorManager
https://developer.android.com/develop/sensors-and-location/sensors/sensors_motion#kotlin

To get started on working with Opengl ES, open this link to understand the basics of how shapes are created and rendered:
---
---
https://developer.android.com/develop/ui/views/graphics/opengl

---
Creating the Sensor class:
```kotlin
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
---
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
