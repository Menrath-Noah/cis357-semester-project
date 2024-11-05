package com.example.prototype_semesterproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData


/*
Helpful links:
https://developer.android.com/reference/kotlin/android/hardware/SensorManager
https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview
https://developer.android.com/reference/kotlin/android/content/Context#SENSOR_SERVICE:kotlin.String

 */



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

}