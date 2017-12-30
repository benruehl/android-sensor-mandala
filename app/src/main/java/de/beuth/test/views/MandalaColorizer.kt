package de.beuth.test.views

import de.beuth.test.adapters.NamedItemsArrayAdapter

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
interface MandalaColorizer : NamedItemsArrayAdapter.NamedAdaptee {
    fun getColor(dataPoint: MandalaDataPoint): Int
}