package de.beuth.test

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import java.io.File
import java.io.PrintWriter

class MainActivity : AppCompatActivity(), SensorEventListener {

    var senSensorManager : SensorManager? = null
    var senAccelerometer : Sensor? = null

    var xValue : TextView? = null
    var yValue : TextView? = null
    var zValue : TextView? = null

    var xBar : ProgressBar? = null
    var yBar : ProgressBar? = null
    var zBar : ProgressBar? = null

    var trigger : Button? = null

    var running = true

    var exportFile : File? = null

    override fun onSensorChanged(p0: SensorEvent?) {
        val mySensor = p0?.sensor

        if (mySensor?.getType() == Sensor.TYPE_ACCELEROMETER) {
            val x = p0?.values[0]
            val y = p0?.values[1]
            val z = p0?.values[2]

            Log.d("_______________________", "")
            Log.d("value X: ", "$x")
            Log.d("value Y: ", "$y")
            Log.d("value Z: ", "$z")
            Log.d("_______________________", "")

            /*exportFile?.printWriter().use { out ->
                out?.println("$x; $y; $z;")
            }*/

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        senSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        senAccelerometer = senSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        senSensorManager?.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL)

        xValue = findViewById(R.id.txtXValue)
        yValue = findViewById(R.id.txtYValue)
        zValue = findViewById(R.id.txtZValue)

        xBar = findViewById(R.id.progressX)
        yBar = findViewById(R.id.progressY)
        zBar = findViewById(R.id.progressZ)

        trigger = findViewById(R.id.button)
        trigger?.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                if(running) {
                    running = false
                } else {
                    running = true
                }
            }
        })

        Log.d("ExportPath", "${Environment.getExternalStorageDirectory()}")
        exportFile = File("${Environment.getExternalStorageDirectory()}Export")
        var success = true
        if (exportFile != null && exportFile!!.exists()) {
            success = exportFile!!.mkdir()
        }
        if (success) {
            val sd = File("filename.txt")

            if (!sd.exists()) {
                success = sd.mkdir()
            }
            if (success) {
                // directory exists or already created
                val dest = File(sd, "export.csv")
                try {
                    PrintWriter(dest).use { out -> out.println("gfdfwd") }
                } catch (e: Exception) {
                    Log.e("ErrorException", e.message)
                }

            } else {
                // directory creation is not successful
            }
        }
    }


    override fun onPause() {
        super.onPause()
        senSensorManager?.unregisterListener(this);
    }

    override fun onResume() {
        super.onResume()
        senSensorManager?.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}