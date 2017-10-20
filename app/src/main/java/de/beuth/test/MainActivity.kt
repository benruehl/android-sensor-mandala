package de.beuth.test

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import java.io.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var senSensorManager : SensorManager? = null
    private var senAccelerometer : Sensor? = null

    private var xValue : TextView? = null
    private var yValue : TextView? = null
    private var zValue : TextView? = null

    private var xBar : ProgressBar? = null
    private var yBar : ProgressBar? = null
    private var zBar : ProgressBar? = null

    var trigger : Button? = null

    private var running = false
    private var accessGranted = false

    private val requestCodeWriteStorage = 0
    private val filename = "export.csv"
    private var exportDir : File? = null
    private var exportFile : File? = null

    override fun onSensorChanged(p0: SensorEvent?) {
        val mySensor = p0?.sensor

        if (mySensor?.getType() == Sensor.TYPE_ACCELEROMETER) {
            val x = p0.values[0]
            val y = p0.values[1]
            val z = p0.values[2]

            /*
            Log.d("_______________________", "")
            Log.d("value X: ", "$x")
            Log.d("value Y: ", "$y")
            Log.d("value Z: ", "$z")
            Log.d("_______________________", "")
            */

            xValue?.setText("$x")
            yValue?.setText("$y")
            zValue?.setText("$z")

            xBar?.setProgress(x.toInt())
            yBar?.setProgress(y.toInt())
            zBar?.setProgress(z.toInt())


            if(accessGranted) {

                try {

                    if(exportDir != null && !exportDir!!.exists())
                        exportDir?.mkdirs()

                    if(exportFile != null && exportFile!!.exists()) {

                        if(running) {
                            val fos = FileOutputStream(exportFile)
                            val fow = OutputStreamWriter(fos)

                            fow.append("$x; $y; $z;\n")
                            fow.close()

                            fos.flush()
                            fos.close()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions , requestCodeWriteStorage)
            }
        } else {
            accessGranted = true
        }

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
        val onClickListener = trigger?.setOnClickListener {
            if (running) {
                running = false
            } else {
                running = true
            }
        }

        exportDir = File("/sdcard/BeuthExport/")
        exportFile = File(exportDir, filename)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessGranted = true;
                } else {
                    accessGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
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
