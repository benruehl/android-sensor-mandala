package de.beuth.test.filters

import android.content.Context
import de.beuth.test.R
import de.beuth.test.sensors.AccelerometerDataPoint

/**
 * Created by Project0rion on 28.12.2017.
 */
class AccelerometerHighPassFilter : SensorFilter<AccelerometerDataPoint> {

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.filter_accelerometer_high_pass)
    }

    //Value from 0 to 1
    private val ALPHA = 0.1f

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint {
        val filteredValues = FloatArray(3)
        val gravity =  FloatArray(3)

        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * dataPoint.x
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * dataPoint.y
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * dataPoint.z

        filteredValues[0] = dataPoint.x - gravity[0]
        filteredValues[1] = dataPoint.y - gravity[1]
        filteredValues[2] = dataPoint.z - gravity[2]

        return AccelerometerDataPoint(filteredValues[0], filteredValues[1], filteredValues[2])
    }
}