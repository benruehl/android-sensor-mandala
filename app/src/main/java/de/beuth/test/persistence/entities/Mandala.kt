package de.beuth.test.persistence.entities

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class Mandala {

    @SerializedName("id") var id: Long = 0
    @SerializedName("surfaceCount") var surfaceCount: Int = 0
    @SerializedName("data") var dataPoints: List<MandalaDataPoint> = ArrayList()
    @SerializedName("colorizer") var colorizerClassFullName: String = ""
    @SerializedName("date") var creationDate: Date = Calendar.getInstance().time
}