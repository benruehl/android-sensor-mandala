package de.beuth.test.views.color

import android.content.Context
import android.graphics.Color
import de.beuth.test.R
import de.beuth.test.views.MandalaDataPoint

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class RainbowColorizer : MandalaColorizer {

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.mandala_colorizer_rainbow)
    }

    override fun getColor(dataPoint: MandalaDataPoint): Int {
        return Color.HSVToColor(floatArrayOf((360 * Math.min(dataPoint.relativeY, 1.0)).toFloat(), 255f, 255f))
    }
}