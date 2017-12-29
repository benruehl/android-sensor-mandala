package de.beuth.test.filters

import de.beuth.test.sensors.AccelerometerDataPoint

/**
 * Created by Benjamin Rühl on 28.12.2017.
 * Passes input directly to output.
 */
class AccelerometerPassAllFilter : SensorFilter<AccelerometerDataPoint> {

    override fun filter(dataPoint: AccelerometerDataPoint): AccelerometerDataPoint {
        return dataPoint;
    }
}