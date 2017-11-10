package de.beuth.test.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import de.beuth.test.R

/**
 * Created by Benjamin Rühl on 03.11.2017.
 */
class MandalaView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val surfaceCount: Int
    private val dataPoints: MutableList<MandalaDataPoint> = ArrayList()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_mandala, this, true)
        val a = getCustomViewAttributes(attrs)

        try {
            surfaceCount = a.getInteger(R.styleable.MandalaView_surfaceCount, 2)
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

        for (surfaceIndex: Int in 0 until surfaceCount) {
            for (dataPointIndex: Int in dataPoints.indices) {
                if (dataPointIndex == 0)
                    continue

                val lastPosition = calcDataPointPosition(dataPoints[dataPointIndex-1], surfaceIndex, mandalaCanvas.width, mandalaCanvas.height)
                val curPosition = calcDataPointPosition(dataPoints[dataPointIndex], surfaceIndex, mandalaCanvas.width, mandalaCanvas.height)

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
        if (surfaceIndex < 0 || surfaceIndex > surfaceCount - 1)
            throw IllegalArgumentException("surfaceIndex out of bounds")

        val viewCenterX = viewWidth / 2
        val viewCenterY = viewHeight / 2

        var positionX: Float
        var positionY: Float

        positionX = (viewCenterX + (viewWidth * dataPoint.relativeY) * Math.sin(calcDataPointAngle(dataPoint, surfaceIndex))).toFloat()
        positionY = (viewCenterY - (viewWidth * dataPoint.relativeY) * Math.cos(calcDataPointAngle(dataPoint, surfaceIndex))).toFloat()

        return Pair<Float, Float>(positionX, positionY)
    }

    private fun calcDataPointAngle(dataPoint: MandalaDataPoint, surfaceIndex: Int): Double {
        if (surfaceIndex < 0 || surfaceIndex > surfaceCount - 1)
            throw IllegalArgumentException("surfaceIndex out of bounds")

        val surfaceAngleRange = (2f * Math.PI) / surfaceCount
        val result = (surfaceIndex * surfaceAngleRange) + (dataPoint.relativeX * surfaceAngleRange)
        return result
    }

    private fun getPaint(dataPoint: MandalaDataPoint): Paint {
        val dataPointPaint = Paint()
        dataPointPaint.style = Paint.Style.STROKE
        dataPointPaint.strokeWidth = dataPoint.thickness.toFloat()
        dataPointPaint.color = Color.BLACK

        return dataPointPaint
    }

    fun addDataPoint(mandalaDataPoint: MandalaDataPoint) {
        dataPoints.add(mandalaDataPoint)
    }
}
