package de.beuth.test

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Created by Benjamin Rühl on 22.10.2017.
 * Wraps around a specified sensor of the android os and provides notification on data changes
 */
class SensorListener(private val sensorManager: SensorManager, private val sensorType: Int) : SensorEventListener {

    private val sensor: Sensor by lazy { sensorManager.getDefaultSensor(sensorType) }

    var onSensorChanged: ((SensorEvent) -> Unit)? = null
    var onAccuracyChanged: ((Sensor, Int) -> Unit)? = null

    var isListening: Boolean = false
        private set

    fun startListening() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        isListening = true
    }

    fun stopListening() {
        sensorManager.unregisterListener(this, sensor)
        isListening = false
    }

    override fun onSensorChanged(e: SensorEvent) {
        onSensorChanged?.invoke(e)
    }

    override fun onAccuracyChanged(s: Sensor, accuracy: Int) {
        onAccuracyChanged?.invoke(s, accuracy)
    }
}