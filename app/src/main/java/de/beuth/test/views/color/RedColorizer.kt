package de.beuth.test.views.color

import android.content.Context
import android.graphics.Color
import de.beuth.test.R
import de.beuth.test.persistence.entities.MandalaDataPoint

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class RedColorizer : MandalaColorizer {

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.mandala_colorizer_red)
    }

    override fun getColor(dataPoint: MandalaDataPoint): Int {
        val redSpectre = (385 - (50 * Math.min(dataPoint.relativeY, 1.0)).toFloat()) % 360
        return Color.HSVToColor(floatArrayOf(redSpectre, 255f, 200f))
    }
}