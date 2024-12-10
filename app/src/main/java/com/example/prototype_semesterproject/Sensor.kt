package com.example.prototype_semesterproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData

val _horizontalData0 = MutableLiveData(0.0)
val horizontalData0: MutableLiveData<Double> get() = _horizontalData0
val _verticalData0 = MutableLiveData(0.0)
val verticalData0: MutableLiveData<Double> get() = _verticalData0
val _zData0 = MutableLiveData(0.0)
val zData0: MutableLiveData<Double> get() = _zData0

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

}