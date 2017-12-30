package de.beuth.test.filters

import android.content.Context

/**
 * Created by Benjamin Rühl on 28.12.2017.
 */
interface SensorFilter<T> {
    fun filter(dataPoint: T): T?
    fun getDisplayName(context: Context): String
}