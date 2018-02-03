package de.beuth.test.filters

import android.content.Context
import de.beuth.test.R
import de.beuth.test.persistence.entities.AccelerometerDataPoint

/**
 * Created by Benjamin Rühl on 28.12.2017.
 */
class AccelerometerLowPassFilter : SensorFilter<AccelerometerDataPoint> {

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.filter_accelerometer_low_pass)
    }

    //Value from 0 to 1
    private val ALPHA = 0.1f

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint {
        val filteredValues = FloatArray(3)

        filteredValues[0] = dataPoint.x * ALPHA + filteredValues[0] * (1.0f - ALPHA)
        filteredValues[1] = dataPoint.y * ALPHA + filteredValues[1] * (1.0f - ALPHA)
        filteredValues[2] = dataPoint.z * ALPHA + filteredValues[2] * (1.0f - ALPHA)

        return AccelerometerDataPoint(filteredValues[0], filteredValues[1], filteredValues[2])
    }
}