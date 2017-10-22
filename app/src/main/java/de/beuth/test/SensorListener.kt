package de.beuth.test

import android.hardware.Sensor
import android.hardware.SensorManager

/**
 * Created by Benjamin Rühl on 22.10.2017.
 */
class SensorListener(private val sensorManager: SensorManager, private val sensorType: Int) {

    private val sensor: Sensor by lazy { sensorManager.getDefaultSensor(sensorType) }

    fun startListening() {

    }
}