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
import de.beuth.test.common.SensorFilter
import de.beuth.test.sensors.Seismograph
import de.beuth.test.sensors.SensorListener
import de.beuth.test.utils.bind
import android.widget.Toast
import android.R.menu
import android.widget.TextView
import com.github.mikephil.charting.data.Entry
import java.lang.Math.sqrt


/**
 * Created by User on 22.10.2017.
 */
class SeismographActivity : AppCompatActivity() {

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    private var seismograph: Seismograph? = null

    private var calibrate : Boolean = false

    private val setFilter: SensorFilter = SensorFilter.NORMAL;

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

            var x = sensorEvent.values[0]
            var y = sensorEvent.values[1]
            var z = sensorEvent.values[2]

            var result : FloatArray = kotlin.FloatArray(3)

            if(setFilter != SensorFilter.NORMAL) {
                if(setFilter == SensorFilter.LOWPASS) {
                    result = lowPassFilter(x, y, z)
                }
                else if(setFilter == SensorFilter.HIGHPASS) {
                    result = highPassFilter(x, y, z)
                }
                else if(setFilter == SensorFilter.NORMALISIZE) {
                    result = normalisizeRotation(x, y, z)
                }

                x = result[0]
                y = result[1]
                z = result[2]
            }

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

    private fun lowPassFilter(x: Float, y: Float, z: Float) : FloatArray {
        val filteredValues = FloatArray(3)

        //Value from 0 to 1
        val ALPHA = 0.1f

        filteredValues[0] = x * ALPHA + filteredValues[0] * (1.0f - ALPHA)
        filteredValues[1] = y * ALPHA + filteredValues[1] * (1.0f - ALPHA)
        filteredValues[2] = z * ALPHA + filteredValues[2] * (1.0f - ALPHA)

        return filteredValues
    }

    private fun highPassFilter(x: Float, y: Float, z: Float) : FloatArray {
        val filteredValues = FloatArray(3)
        val gravity =  FloatArray(3)

        //Value from 0 to 1
        val ALPHA = 0.1f

        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * x;
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * y;
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * z;

        filteredValues[0] = x - gravity[0];
        filteredValues[1] = y - gravity[1];
        filteredValues[2] = z - gravity[2];

        return filteredValues;
    }

    private fun normalisizeRotation(axisX: Float, axisY: Float, axisZ: Float) : FloatArray {
        val omegaMagnitude = sqrt((axisX * axisX + axisY * axisY + axisY * axisZ).toDouble())

        axisX.div(omegaMagnitude.toFloat())
        axisY.div(omegaMagnitude.toFloat())
        axisZ.div(omegaMagnitude.toFloat())

        return floatArrayOf(axisX, axisY, axisZ)
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