package de.beuth.test.persistence.entities

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaDataPoint {

    var relativeX: Double = 0.0

    var relativeY: Double = 0.0

    var thickness: Double = 1.0

    constructor(relativeX: Double, relativeY: Double, thickness: Double) {
        this.relativeX = relativeX
        this.relativeY = relativeY
        this.thickness = thickness
    }

    override fun toString(): String {
        return "${this.javaClass.name}[$relativeX, $relativeY, $thickness]"
    }
}