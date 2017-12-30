package de.beuth.test.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import de.beuth.test.R
import de.beuth.test.views.color.BlackColorizer

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val dataPoints: MutableList<MandalaDataPoint> = ArrayList()

    private val useMirroring = true

    var surfaceCount: Int

    var maxDataPointCount: Int

    var colorizer: MandalaColorizer = BlackColorizer()
        set(value) {
            invalidate()
            field = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_mandala, this, true)
        val a = getCustomViewAttributes(attrs)

        try {
            surfaceCount = a.getInteger(R.styleable.MandalaView_surfaceCount, 2)
            maxDataPointCount = 8192 / surfaceCount
        } finally {
            a.recycle()
        }
    }

    private fun getCustomViewAttributes(attrs: AttributeSet): TypedArray {
        return context.theme.obtainStyledAttributes(attrs, R.styleable.MandalaView, 0, 0)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val mandalaCanvas: Canvas = canvas ?: Canvas()

        drawCenter(mandalaCanvas)

        val visualPointsPerDataPoint: Int = if (useMirroring) surfaceCount * 2 else surfaceCount;

        for (surfaceIndex: Int in 0 until visualPointsPerDataPoint) {
            for (dataPointIndex: Int in dataPoints.indices) {
                if (dataPointIndex == 0)
                    continue

                var lastDataPoint = dataPoints[dataPointIndex-1]
                var curDataPoint = dataPoints[dataPointIndex]

                if (useMirroring && surfaceIndex % 2 == 1) {
                    lastDataPoint = MandalaDataPoint(1 - lastDataPoint.relativeX, lastDataPoint.relativeY, lastDataPoint.thickness)
                    curDataPoint = MandalaDataPoint(1 - curDataPoint.relativeX, curDataPoint.relativeY, curDataPoint.thickness)
                }

                val lastPosition = calcDataPointPosition(lastDataPoint, surfaceIndex, mandalaCanvas.width, mandalaCanvas.height)
                val curPosition = calcDataPointPosition(curDataPoint, surfaceIndex, mandalaCanvas.width, mandalaCanvas.height)

                mandalaCanvas.drawLine(lastPosition.first, lastPosition.second, curPosition.first, curPosition.second, getPaint(dataPoints[dataPointIndex]))
            }
        }
    }

    private fun drawCenter(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL

        canvas.drawCircle(canvas.width / 2f, canvas.height / 2f, 10f, paint)
    }

    private fun calcDataPointPosition(dataPoint: MandalaDataPoint, surfaceIndex: Int, viewWidth: Int, viewHeight: Int): Pair<Float, Float> {
        val viewCenterX = viewWidth / 2
        val viewCenterY = viewHeight / 2

        val maxDrawingRadius = viewWidth / 2
        val dataPointRadius = maxDrawingRadius * dataPoint.relativeY
        val dataPointAngle = calcDataPointAngle(dataPoint, surfaceIndex)

        var positionX = (viewCenterX + dataPointRadius * Math.sin(dataPointAngle)).toFloat()
        var positionY = (viewCenterY - dataPointRadius * Math.cos(dataPointAngle)).toFloat()

        return Pair<Float, Float>(positionX, positionY)
    }

    private fun calcDataPointAngle(dataPoint: MandalaDataPoint, surfaceIndex: Int): Double {
        val relevantSurfaceCount: Int = if (useMirroring) surfaceCount * 2 else surfaceCount;

        if (surfaceIndex < 0 || surfaceIndex > relevantSurfaceCount- 1)
            throw IllegalArgumentException("surfaceIndex out of bounds")

        val surfaceAngleRange = (2f * Math.PI) / relevantSurfaceCount
        val result = (surfaceIndex * surfaceAngleRange) + (dataPoint.relativeX * surfaceAngleRange)
        return result
    }

    private fun getPaint(dataPoint: MandalaDataPoint): Paint {
        val dataPointPaint = Paint()
        dataPointPaint.style = Paint.Style.STROKE
        dataPointPaint.strokeWidth = dataPoint.thickness.toFloat()
        dataPointPaint.color = colorizer.getColor(dataPoint)

        return dataPointPaint
    }

    fun addDataPoint(mandalaDataPoint: MandalaDataPoint) {
        dataPoints.add(mandalaDataPoint)
        ensureMaxDataPointCount()
        invalidate()
    }

    private fun ensureMaxDataPointCount() {
        while (dataPoints.size > maxDataPointCount)
            dataPoints.removeAt(0)
    }
}
