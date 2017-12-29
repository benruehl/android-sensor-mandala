package de.beuth.test.filters

/**
 * Created by Benjamin Rühl on 28.12.2017.
 */
interface SensorFilter<T> {
    fun filter(dataPoint: T): T
}