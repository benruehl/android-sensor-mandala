package de.beuth.test.filters

import de.beuth.test.sensors.AccelerometerDataPoint

/**
 * Created by Benjamin Rühl on 28.12.2017.
 */
class AccelerometerNormalizeFilter : SensorFilter<AccelerometerDataPoint> {

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint {
        val omegaMagnitude = Math.sqrt((dataPoint.x * dataPoint.x + dataPoint.y * dataPoint.y + dataPoint.z * dataPoint.z).toDouble())

        val newX = dataPoint.x / omegaMagnitude.toFloat()
        val newY = dataPoint.y / omegaMagnitude.toFloat()
        val newZ = dataPoint.z / omegaMagnitude.toFloat()

        return AccelerometerDataPoint(newX, newY, newZ)
    }
}