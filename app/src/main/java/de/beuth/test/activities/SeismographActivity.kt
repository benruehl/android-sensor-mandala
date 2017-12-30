package de.beuth.test.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.github.mikephil.charting.charts.LineChart
import de.beuth.test.R
import de.beuth.test.sensors.Seismograph
import de.beuth.test.sensors.SensorListener
import de.beuth.test.utils.bind
import android.widget.TextView
import de.beuth.test.adapters.NamedItemsArrayAdapter
import de.beuth.test.filters.*
import de.beuth.test.sensors.AccelerometerDataPoint


/**
 * Created by User on 22.10.2017.
 */
class SeismographActivity : AppCompatActivity() {

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    private var seismograph: Seismograph? = null

    private var calibrate : Boolean = false

    private val availableAccelerometerFilters = listOf<SensorFilter<AccelerometerDataPoint>>(
            AccelerometerPassAllFilter(),
            AccelerometerHighPassFilter(),
            AccelerometerLowPassFilter(),
            AccelerometerNormalizeFilter()
    )

    private var currentAccelerometerFilter: SensorFilter<AccelerometerDataPoint> = availableAccelerometerFilters.first()

    private val filterSelectionSpinner: Spinner by bind(R.id.seismographFilterSelectionSpinner)

    private val chartX : LineChart by bind(R.id._chartX)
    private val chartY : LineChart by bind(R.id._chartY)
    private val chartZ : LineChart by bind(R.id._chartZ)

    private val textX : TextView by bind(R.id._txtChartX)
    private val textY : TextView by bind(R.id._txtChartY)
    private val textZ : TextView by bind(R.id._txtChartZ)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seismograph)

        seismograph = Seismograph(chartX, chartY, chartZ, textX, textY, textZ)
        seismograph!!.initCharts()

        for (i in 1..seismograph!!.dataLimit) {
            seismograph!!.addData(0f, 0f, 0f)
        }

        initFilterSelectionSpinner()

        sensorListener.onSensorChanged = { sensorEvent: SensorEvent -> onAccelerometerChanged(sensorEvent) }
        sensorListener.startListening()
    }

    private fun onAccelerometerChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val sensorDataPoint = AccelerometerDataPoint(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2])
            val filteredSensorDataPoint = currentAccelerometerFilter.filter(sensorDataPoint) ?: return

            seismograph?.addData(filteredSensorDataPoint.x, filteredSensorDataPoint.y, filteredSensorDataPoint.z)

            if (calibrate) {
                seismograph?.setFixValues(filteredSensorDataPoint.x, filteredSensorDataPoint.y, filteredSensorDataPoint.z)
                calibrate = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.seismo_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == R.id.action_seismo_calibrate) {
            calibrate = true;
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFilterSelectionSpinner() {
        filterSelectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentAccelerometerFilter = parent?.getItemAtPosition(position) as SensorFilter<AccelerometerDataPoint>
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val spinnerAdapter = NamedItemsArrayAdapter(this, android.R.layout.simple_spinner_item, availableAccelerometerFilters)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSelectionSpinner.adapter = spinnerAdapter
    }

    override fun onPause() {
        super.onPause()
        if (sensorListener.isListening)
            sensorListener.stopListening()
    }

    override fun onResume() {
        super.onResume()
        sensorListener.startListening()
    }

}