package de.beuth.test.filters

import android.content.Context
import de.beuth.test.R
import de.beuth.test.sensors.AccelerometerDataPoint

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class AccelerometerReduceCloseNeighborsFilter(val minNeighborDistance: Float)
    : SensorFilter<AccelerometerDataPoint> {

    private var lastDataPoint: AccelerometerDataPoint? = null

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.filter_accelerometer_reduce_close_neighbors) + " ($minNeighborDistance)"
    }

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint? {
        var result: AccelerometerDataPoint? = dataPoint

        if (lastDataPoint != null) {
            val diffX = (lastDataPoint!!.x - dataPoint.x).toDouble()
            val diffY = (lastDataPoint!!.y - dataPoint.y).toDouble()
            val diffZ = (lastDataPoint!!.z - dataPoint.z).toDouble()
            val distanceToLastDataPoint = Math.sqrt((diffX * diffX) + (diffY * diffY) + (diffZ * diffZ))

            if (distanceToLastDataPoint < minNeighborDistance)
                result = null
        }

        lastDataPoint = dataPoint

        return result
    }
}