package de.beuth.test.views.color

import android.content.Context
import android.graphics.Color
import de.beuth.test.R
import de.beuth.test.views.MandalaColorizer
import de.beuth.test.views.MandalaDataPoint

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class BlackColorizer : MandalaColorizer {

    override fun getDisplayName(context: Context): String {
        return context.getString(R.string.mandala_colorizer_black)
    }

    override fun getColor(dataPoint: MandalaDataPoint): Int {
        return Color.BLACK
    }
}