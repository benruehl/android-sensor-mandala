package de.beuth.test

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart

/**
 * Created by User on 22.10.2017.
 */
class SeismographActivity : AppCompatActivity(), SensorEventListener {

    private var senSensorManager : SensorManager? = null
    private var senAccelerometer : Sensor? = null

    private var seismograph: Seismograph? = null

    private val chartX : LineChart by bind(R.id._chartX)
    private val chartY : LineChart by bind(R.id._chartY)
    private val chartZ : LineChart by bind(R.id._chartZ)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seismograph)

        seismograph = Seismograph(chartX, chartY, chartZ)
        seismograph!!.initCharts()

        senSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        senAccelerometer = senSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        senSensorManager?.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val mySensor = event?.sensor

        if (mySensor?.getType() == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            seismograph?.addData(x,y,z)

            println(seismograph?.getDataX())
            println(seismograph?.getDataY())
            println(seismograph?.getDataZ())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause() {
        super.onPause()
        senSensorManager?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        senSensorManager?.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

}