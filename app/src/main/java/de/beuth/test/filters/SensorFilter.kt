package de.beuth.test.filters

import de.beuth.test.adapters.NamedItemsArrayAdapter

/**
 * Created by Benjamin Rühl on 28.12.2017.
 */
interface SensorFilter<T> : NamedItemsArrayAdapter.NamedAdaptee {
    fun filter(dataPoint: T): T?
}