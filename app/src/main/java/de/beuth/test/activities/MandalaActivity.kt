package de.beuth.test.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.beuth.test.R
import de.beuth.test.sensors.SensorListener
import de.beuth.test.utils.bind
import de.beuth.test.views.MandalaDataPoint
import de.beuth.test.views.MandalaView

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaActivity : AppCompatActivity() {

    private val mandalaView: MandalaView by bind(R.id.mandalaView)

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala)

        sensorListener.onSensorChanged = { sensorEvent: SensorEvent -> onAccelerometerChanged(sensorEvent) }
        sensorListener.startListening()

//      generateDataPoints()
    }

    private fun onAccelerometerChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = sensorEvent.values[0]
            val y = sensorEvent.values[1]
            val z = sensorEvent.values[2]

            val sensorDataPoint = MandalaDataPoint(x / 20.0, y / 20.0, 2.0 + z / 20)

            mandalaView.addDataPoint(sensorDataPoint)
        }
    }

    private fun generateDataPoints() {
        for (i in 0..100) {
            var dataPoint = MandalaDataPoint(i/100.0, i/100.0, i.toDouble())
            mandalaView.addDataPoint(dataPoint)
        }
    }
}