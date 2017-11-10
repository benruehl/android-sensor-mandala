package de.beuth.test.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.beuth.test.R
import de.beuth.test.utils.bind
import de.beuth.test.views.MandalaDataPoint
import de.beuth.test.views.MandalaView

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaActivity : AppCompatActivity() {

    private val mandalaView: MandalaView by bind(R.id.mandalaView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandala)

        generateDataPoints()
    }

    private fun generateDataPoints() {
        for (i in 0..10) {
            var dataPoint = MandalaDataPoint(i/10.0, i/10.0, 1.0)
            mandalaView.addDataPoint(dataPoint)
        }
    }
}