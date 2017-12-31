package de.beuth.test.views.color

import de.beuth.test.adapters.NamedItemsArrayAdapter
import de.beuth.test.persistence.entities.MandalaDataPoint

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
interface MandalaColorizer : NamedItemsArrayAdapter.NamedAdaptee {
    fun getColor(dataPoint: MandalaDataPoint): Int
}