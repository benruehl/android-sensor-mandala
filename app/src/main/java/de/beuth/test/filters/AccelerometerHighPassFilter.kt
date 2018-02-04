package de.beuth.test.filters

import android.content.Context
import de.beuth.test.R
import de.beuth.test.persistence.entities.AccelerometerDataPoint

/**
 * Created by Project0rion on 28.12.2017.
 */
class AccelerometerHighPassFilter : SensorFilter<AccelerometerDataPoint> {

    private var lastDataPoint: AccelerometerDataPoint? = null

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.filter_accelerometer_high_pass)
    }

    //Value from 0 to 1
    private val ALPHA = 0.5f

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint {
        if (lastDataPoint == null) {
            return dataPoint;
        }

        var diffX = ALPHA * lastDataPoint!!.x + (1 - ALPHA) * dataPoint.x
        var diffY = ALPHA * lastDataPoint!!.y + (1 - ALPHA) * dataPoint.y
        var diffZ = ALPHA * lastDataPoint!!.z + (1 - ALPHA) * dataPoint.z

        diffX = dataPoint.x - diffX
        diffY = dataPoint.y - diffY
        diffZ = dataPoint.z - diffZ

        return AccelerometerDataPoint(diffX, diffY, diffZ)
    }
}