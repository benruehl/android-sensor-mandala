package de.beuth.test.filters

import android.content.Context
import de.beuth.test.R
import de.beuth.test.persistence.entities.AccelerometerDataPoint

/**
 * Created by Benjamin Rühl on 28.12.2017.
 */
class AccelerometerLowPassFilter : SensorFilter<AccelerometerDataPoint> {

    private var lastDataPoint: AccelerometerDataPoint? = null

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.filter_accelerometer_low_pass)
    }

    //Value from 0 to 1
    private val ALPHA = 0.5f

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint? {
        if (lastDataPoint == null) {
            return dataPoint;
        }

        val diffX = lastDataPoint!!.x + ALPHA * (dataPoint.x - lastDataPoint!!.x);
        val diffY = lastDataPoint!!.y + ALPHA * (dataPoint.x - lastDataPoint!!.y);
        val diffZ = lastDataPoint!!.z + ALPHA * (dataPoint.x - lastDataPoint!!.z);

        return AccelerometerDataPoint(diffX, diffY, diffZ)
    }
}