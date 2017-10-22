package de.beuth.test

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val sensorListener: SensorListener by lazy {
        SensorListener(getSystemService(Context.SENSOR_SERVICE) as SensorManager, Sensor.TYPE_ACCELEROMETER)
    }

    private val fileExporter: FileExporter by lazy { FileExporter("/sdcard/BeuthExport/", "export.csv") }

    private val xValue: TextView by bind(R.id.txtXValue)
    private val yValue: TextView by bind(R.id.txtYValue)
    private val zValue: TextView by bind(R.id.txtZValue)

    private val xBar: ProgressBar by bind(R.id.progressX)
    private val yBar: ProgressBar by bind(R.id.progressY)
    private val zBar: ProgressBar by bind(R.id.progressZ)

    private val trigger: Button by bind(R.id.button)
    private val seismo: Button by bind(R.id.startSeismograph)

    private var writingPermissionsGranted = false
    private val requestCodeWriteStorage = 0

    private var isRecordingSensorData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        writingPermissionsGranted = fileExporter.checkStorageWritingPermission(this);

        if (!writingPermissionsGranted)
            ensureStorageWritingPermissionsGranted()

        sensorListener.onSensorChanged = { sensorEvent: SensorEvent -> onAccelerometerChanged(sensorEvent) }
        sensorListener.startListening()

        trigger.setOnClickListener {
            isRecordingSensorData = !isRecordingSensorData
        }
    }

    private fun onAccelerometerChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = sensorEvent.values[0]
            val y = sensorEvent.values[1]
            val z = sensorEvent.values[2]

            xValue.text = "$x"
            yValue.text = "$y"
            zValue.text = "$z"

            xBar.progress = x.toInt()
            yBar.progress = y.toInt()
            zBar.progress = z.toInt()

            if (writingPermissionsGranted && isRecordingSensorData) {
                fileExporter.appendToFile("$x; $y; $z;${System.getProperty("line.separator")}")
            }
        }
    }

    private fun ensureStorageWritingPermissionsGranted() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, fileExporter.requiredPermission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, arrayOf(fileExporter.requiredPermission), requestCodeWriteStorage)
        }

        seismo.setOnClickListener {
            val intent : Intent = Intent()
            intent.setClass(this, SeismographActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                writingPermissionsGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                return
            }
        }
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
