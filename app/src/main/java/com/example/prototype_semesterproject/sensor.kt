package com.example.prototype_semesterproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData

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
//    fun stopSensors() {
//        // Unregister sensor listeners
//        mSensorManager.unregisterListener(sensorEventListener)
//    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        sensorData.postValue(event.values)
    }

    fun changeSensorData(newSensorData: FloatArray) {
        if (newSensorData.isNotEmpty()) {
            var horizontal = newSensorData[0]
            var vertical = newSensorData[1]
            var z = newSensorData[2]
            _horizontalData.postValue(horizontal.toDouble())
            _verticalData.postValue(vertical.toDouble())
            _zData.postValue(z.toDouble())
        }
    }

}