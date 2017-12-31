package de.beuth.test.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import de.beuth.test.R
import de.beuth.test.adapters.NamedItemsArrayAdapter
import de.beuth.test.filters.*
import de.beuth.test.persistence.access.DAOFactoryService
import de.beuth.test.persistence.entities.Mandala
import de.beuth.test.sensors.AccelerometerDataPoint
import de.beuth.test.sensors.SensorListener
import de.beuth.test.utils.bind
import de.beuth.test.views.color.MandalaColorizer
import de.beuth.test.persistence.entities.MandalaDataPoint
import de.beuth.test.views.MandalaView
import de.beuth.test.views.color.BlackColorizer
import de.beuth.test.views.color.RainbowColorizer
import de.beuth.test.views.color.RedColorizer
import kotlinx.android.synthetic.main.activity_mandala_generator.*

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaGeneratorActivity : AppCompatActivity() {

    private val mandalaView: MandalaView by bind(R.id.mandalaGenerationView)

    private val availableAccelerometerFilters = listOf<SensorFilter<AccelerometerDataPoint>>(
            AccelerometerReduceCloseNeighborsFilter(1f),
            AccelerometerReduceCloseNeighborsFilter(10f),
            AccelerometerHighPassFilter(),
            AccelerometerLowPassFilter(),
            AccelerometerNormalizeFilter(),
            AccelerometerPassAllFilter()
    )

    private var currentAccelerometerFilter: SensorFilter<AccelerometerDataPoint> = availableAccelerometerFilters.first()

    private val filterSelectionSpinner: Spinner by bind(R.id.mandalaFilterSelectionSpinner)

    private val availableMandalaColorizers = listOf(
            BlackColorizer(),
            RedColorizer(),
            RainbowColorizer()
    )

    private val colorizerSelectionSpinner: Spinner by bind(R.id.mandalaColorizerSelectionSpinner)

    private val mandalaTakeSnapshotButton: Button by bind(R.id.mandalaTakeSnapshotButton)

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala_generator)

        initFilterSelectionSpinner()
        initColorizerSelectionSpinner()

        sensorListener.onSensorChanged = { sensorEvent: SensorEvent -> onAccelerometerChanged(sensorEvent) }
        sensorListener.startListening()

        mandalaTakeSnapshotButton.setOnClickListener { createMandalaSnapshot() }

//      generateDataPoints()
    }

    private fun onAccelerometerChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val sensorDataPoint = AccelerometerDataPoint(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2])
            val filteredSensorDataPoint = currentAccelerometerFilter.filter(sensorDataPoint) ?: return

            val mandalaDataPoint = MandalaDataPoint(
                    filteredSensorDataPoint.x / 20.0,
                    filteredSensorDataPoint.y / 20.0,
                    2.0 + filteredSensorDataPoint.z / 10
            )

            mandalaView.addDataPoint(mandalaDataPoint)
        }
    }

    private fun generateDataPoints() {
        for (i in 0..100) {
            var dataPoint = MandalaDataPoint(i / 100.0, i / 100.0, i.toDouble())
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

        val spinnerAdapter = NamedItemsArrayAdapter(this, android.R.layout.simple_spinner_item, availableAccelerometerFilters)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSelectionSpinner.adapter = spinnerAdapter
    }

    private fun updateMaxDataPointCountForCurrentFilter() {
        if (currentAccelerometerFilter is AccelerometerReduceCloseNeighborsFilter)
            mandalaView.maxDataPointCount = 1024 / mandalaView.surfaceCount
        else
            mandalaView.maxDataPointCount = 8192 / mandalaView.surfaceCount
    }

    private fun initColorizerSelectionSpinner() {
        colorizerSelectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mandalaView.colorizer = parent?.getItemAtPosition(position) as MandalaColorizer
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val spinnerAdapter = NamedItemsArrayAdapter(this, android.R.layout.simple_spinner_item, availableMandalaColorizers)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mandalaColorizerSelectionSpinner.adapter = spinnerAdapter
    }

    private fun createMandalaSnapshot() {
        var mandalaSnapshot = Mandala()
        mandalaSnapshot.surfaceCount = mandalaView.surfaceCount
        mandalaSnapshot.dataPoints = mandalaView.dataPointsReadOnly
        mandalaSnapshot.colorizerClassFullName = mandalaView.colorizer.javaClass.name

        DAOFactoryService.daoFactory.getMandalaDAO().save(mandalaSnapshot)
    }
}