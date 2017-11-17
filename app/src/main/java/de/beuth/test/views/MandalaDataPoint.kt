package de.beuth.test.views

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaDataPoint {

    var relativeX: Double = 0.0
        private set

    var relativeY: Double = 0.0
        private set

    var thickness: Double = 1.0
        private set

    constructor(relativeX: Double, relativeY: Double, thickness: Double) {
        this.relativeX = relativeX
        this.relativeY = relativeY
        this.thickness = thickness
    }

    override fun toString(): String {
        return "${this.javaClass.name}[$relativeX, $relativeY, $thickness]"
    }
}