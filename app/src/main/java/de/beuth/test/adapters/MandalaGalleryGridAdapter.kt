package de.beuth.test.adapters

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.beuth.test.filters.AccelerometerReduceCloseNeighborsFilter
import de.beuth.test.persistence.entities.Mandala
import de.beuth.test.persistence.entities.MandalaDataPoint
import de.beuth.test.views.MandalaView
import de.beuth.test.views.color.MandalaColorizer

/**
 * Created by Benjamin Rühl on 31.12.2017.
 */
class MandalaGalleryGridAdapter(context: Context?, resource: Int, objects: List<Mandala>?)
    : ArrayAdapter<Mandala>(context, resource, objects) {

    private var items: List<Mandala> = objects ?: ArrayList()

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Mandala {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val currentItem = getItem(position)
        val preview = MandalaView(context)

        preview.surfaceCount = currentItem.surfaceCount
        preview.maxDataPointCount = Int.MAX_VALUE
        preview.addDataPoints(filterDataPointsForPreview(currentItem.dataPoints))

        if (currentItem.colorizerClassFullName.isNotEmpty())
            preview.colorizer = Class.forName(currentItem.colorizerClassFullName).getConstructor().newInstance() as MandalaColorizer

        preview.minHeight = 600
        preview.setBackgroundColor(Color.TRANSPARENT) // seems to be important!

        return preview
    }

    private fun filterDataPointsForPreview(dataPoints: List<MandalaDataPoint>): Iterable<MandalaDataPoint> {
        val minDataPointDelta = 0.1f
        val maxCloseLinesCount = 1

        val filteredDataPoints = mutableListOf<MandalaDataPoint>()
        val dataPointLines = dataPoints.indices.map { if (it != 0) Pair(dataPoints[it - 1], dataPoints[it]) else Pair(dataPoints[it], dataPoints[it]) }

        for (i: Int in dataPoints.indices) {
            if (i == 0)
                continue

            if (areDataPointsTooClose(dataPoints[i], dataPoints[i - 1], minDataPointDelta))
                continue

            if (dataPointLines.filter { dpl -> areDataPointLinesTooClose(dpl, Pair(dataPoints[i], dataPoints[i - 1])) }.count() > maxCloseLinesCount)
                continue

            filteredDataPoints.add(dataPoints[i])
        }

        return filteredDataPoints
    }

    private fun areDataPointsTooClose(dataPoint1: MandalaDataPoint, dataPoint2: MandalaDataPoint, minDelta: Float): Boolean {
        val diffX = (dataPoint1.relativeX - dataPoint2.relativeX)
        val diffY = (dataPoint1.relativeY - dataPoint2.relativeY)
        val distanceToLastDataPoint = Math.sqrt((diffX * diffX) + (diffY * diffY))

        return distanceToLastDataPoint < minDelta
    }

    private fun areDataPointLinesTooClose(dataPointLine1: Pair<MandalaDataPoint, MandalaDataPoint>, dataPointLine2: Pair<MandalaDataPoint, MandalaDataPoint>): Boolean {
        val minDelta = 0.2f

        return areDataPointsTooClose(dataPointLine1.first, dataPointLine2.first, minDelta)
                && areDataPointsTooClose(dataPointLine1.second, dataPointLine2.second, minDelta)
    }
}