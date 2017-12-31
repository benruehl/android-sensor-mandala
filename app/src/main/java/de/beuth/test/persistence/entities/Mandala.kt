package de.beuth.test.persistence.entities

import java.util.*

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class Mandala {

    var id: Long = 0
    var surfaceCount: Int = 0
    var dataPoints: List<MandalaDataPoint> = ArrayList()
    var colorizerClassFullName: String = ""
    var creationDate: Date = Calendar.getInstance().time
}