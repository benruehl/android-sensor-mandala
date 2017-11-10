package de.beuth.test.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.github.mikephil.charting.charts.LineChart
import de.beuth.test.R
import de.beuth.test.sensors.Seismograph
import de.beuth.test.sensors.SensorListener
import de.beuth.test.utils.bind
import android.widget.Toast
import android.R.menu
import android.widget.TextView
import com.github.mikephil.charting.data.Entry


/**
 * Created by User on 22.10.2017.
 */
class SeismographActivity : AppCompatActivity() {

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    private var seismograph: Seismograph? = null

    private var calibrate : Boolean = false

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

        sensorListener.onSensorChanged = { sensorEvent: SensorEvent -> onAccelerometerChanged(sensorEvent) }
        sensorListener.startListening()
    }

    private fun onAccelerometerChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = sensorEvent.values[0]
            val y = sensorEvent.values[1]
            val z = sensorEvent.values[2]

            seismograph?.addData(x,y,z)

            if(calibrate) {
                seismograph?.setFixValues(x,y,z)
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