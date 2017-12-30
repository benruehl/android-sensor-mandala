package de.beuth.test.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import de.beuth.test.R
import de.beuth.test.adapters.SensorFilterArrayAdapter
import de.beuth.test.filters.*
import de.beuth.test.sensors.AccelerometerDataPoint
import de.beuth.test.sensors.SensorListener
import de.beuth.test.utils.bind
import de.beuth.test.views.MandalaDataPoint
import de.beuth.test.views.MandalaView

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaActivity : AppCompatActivity() {

    private val mandalaView: MandalaView by bind(R.id.mandalaView)

    private val availableAccelerometerFilters = listOf<SensorFilter<AccelerometerDataPoint>>(
            AccelerometerPassAllFilter(),
            AccelerometerHighPassFilter(),
            AccelerometerLowPassFilter(),
            AccelerometerNormalizeFilter(),
            AccelerometerReduceCloseNeighborsFilter(1f),
            AccelerometerReduceCloseNeighborsFilter(10f)
    )

    private var currentAccelerometerFilter: SensorFilter<AccelerometerDataPoint> = availableAccelerometerFilters.first()

    private val filterSelectionSpinner: Spinner by bind(R.id.mandalaFilterSelectionSpinner)

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala)

        initFilterSelectionSpinner()

        sensorListener.onSensorChanged = { sensorEvent: SensorEvent -> onAccelerometerChanged(sensorEvent) }
        sensorListener.startListening()

//      generateDataPoints()
    }

    private fun onAccelerometerChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val sensorDataPoint = AccelerometerDataPoint(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2])
            val filteredSensorDataPoint = currentAccelerometerFilter.filter(sensorDataPoint) ?: return

            val mandalaDataPoint = MandalaDataPoint(
                    filteredSensorDataPoint.x / 20.0,
                    filteredSensorDataPoint.y / 20.0,
                    2.0 + filteredSensorDataPoint.z / 20
            )

            mandalaView.addDataPoint(mandalaDataPoint)
        }
    }

    private fun generateDataPoints() {
        for (i in 0..100) {
            var dataPoint = MandalaDataPoint(i/100.0, i/100.0, i.toDouble())
            mandalaView.addDataPoint(dataPoint)
        }
    }

    private fun initFilterSelectionSpinner() {
        filterSelectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentAccelerometerFilter = parent?.getItemAtPosition(position) as SensorFilter<AccelerometerDataPoint>

                updateMaxDataPointCountForCurrentFilter()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val spinnerAdapter = SensorFilterArrayAdapter(this, android.R.layout.simple_spinner_item, availableAccelerometerFilters)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSelectionSpinner.adapter = spinnerAdapter
    }

    private fun updateMaxDataPointCountForCurrentFilter() {
        if (currentAccelerometerFilter is AccelerometerReduceCloseNeighborsFilter)
            mandalaView.maxDataPointCount = 1024 / mandalaView.surfaceCount
        else
            mandalaView.maxDataPointCount = 8192 / mandalaView.surfaceCount
    }
}