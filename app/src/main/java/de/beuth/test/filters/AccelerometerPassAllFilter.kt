package de.beuth.test.filters

import android.content.Context
import de.beuth.test.R
import de.beuth.test.persistence.entities.AccelerometerDataPoint

/**
 * Created by Benjamin Rühl on 28.12.2017.
 * Passes input directly to output.
 */
class AccelerometerPassAllFilter : SensorFilter<AccelerometerDataPoint> {

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.filter_accelerometer_pass_all)
    }

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint {
        return dataPoint;
    }
}