package de.beuth.test.sensors

import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


/**
 * Created by User on 22.10.2017.
 */
class Seismograph (xChart : LineChart, yChart : LineChart, zChart : LineChart,
                   xText : TextView, yText : TextView, zText : TextView) {

    private val chartX : LineChart by lazy {xChart}
    private val chartY : LineChart by lazy {yChart}
    private val chartZ : LineChart by lazy {zChart}

    private val textX : TextView by lazy {xText}
    private val textY : TextView by lazy {yText}
    private val textZ : TextView by lazy {zText}

    private var sensorDataX: MutableList<Entry> = mutableListOf()
    private var sensorDataY: MutableList<Entry> = mutableListOf()
    private var sensorDataZ: MutableList<Entry> = mutableListOf()

    private var fixValueX : Float? = null
    private var fixValueY : Float? = null
    private var fixValueZ : Float? = null

    val dataLimit : Int = 500

    fun initCharts() {
        setupChart(chartX)
        setupChart(chartY)
        setupChart(chartZ)
    }

    fun setDataX(data: MutableList<Entry>) {
        sensorDataX = data;
    }

    fun setDataY(data: MutableList<Entry>) {
        sensorDataY = data;
    }

    fun setDataZ(data: MutableList<Entry>) {
        sensorDataZ = data;
    }

    fun getDataX() : MutableList<Entry> {
        return sensorDataX
    }

    fun getDataY() : MutableList<Entry> {
        return sensorDataY
    }

    fun getDataZ() : MutableList<Entry> {
        return sensorDataZ
    }

    fun setFixValues(valueX : Float, valueY : Float, valueZ : Float) {
        fixValueX = valueX
        fixValueY = valueY
        fixValueZ = valueZ
    }

    fun addData (valueX : Float, valueY : Float, valueZ : Float) {

        if(fixValueX ==  null)
            fixValueX = valueX

        if(fixValueY == null)
            fixValueY = valueY

        if(fixValueZ == null)
            fixValueZ = valueZ

        var diffX = fixValueX!! - valueX
        var diffY = fixValueY!! - valueY
        var diffZ = fixValueZ!! - valueZ

        diffX = calculateValue(diffX)
        diffY = calculateValue(diffY)
        diffZ = calculateValue(diffZ)

        val formatX = String.format("%.2f", diffX)
        val formatY = String.format("%.2f", diffY)
        val formatZ = String.format("%.2f", diffZ)

        textX.setText(formatX)
        textY.setText(formatY)
        textZ.setText(formatZ)

        refreshChartData(chartX, diffX)
        refreshChartData(chartY, diffY)
        refreshChartData(chartZ, diffZ)
    }

    fun setupChart(chart : LineChart) {
        val xAxis : XAxis = chart.xAxis
        xAxis.setEnabled(false)

        val left = chart.axisLeft
        left.setDrawLabels(false) // no axis labels
        left.setDrawAxisLine(false) // no axis line
        left.setDrawGridLines(false) // no grid lines
        left.setDrawZeroLine(true) // draw a zero line
        left.zeroLineColor = ColorTemplate.rgb("#FE1832")
        left.axisMaximum = 15f;
        left.axisMinimum = -15f;
        left.spaceTop = 10f;
        left.spaceBottom = 10f;
        chart.axisRight.setEnabled(false)

        val legend : Legend = chart.legend
        legend.setEnabled(false)

        chart.setDrawGridBackground(false)
        chart.getDescription().setEnabled(false)
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);

        val lineData = LineData()

        chart.setData(lineData)
        chart.invalidate();
        //TODO Styling der Charts
    }

    fun calculateValue(value : Float) : Float {
        var temp : Float = value

        if (temp > 9) {
            temp = 9 + (value / 10)
            return temp
        }

        if(temp < -9) {
            temp = -9 - (value / 10)
            return temp
        }

        return temp
    }

    fun createDateSet() : LineDataSet {
        val dataSet = LineDataSet(null, "SensorData")
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawHighlightIndicators(false)
        dataSet.color = ColorTemplate.rgb("#268AF4")
        dataSet.lineWidth = 2f
        dataSet.cubicIntensity = 3f

        return dataSet
    }

    fun refreshChartData(chart : LineChart, value : Float) {

        val data = chart.getData()

        var set: ILineDataSet? = data.getDataSetByIndex(0)

        if (set == null) {
            set = createDateSet()
            data.addDataSet(set)
        }

        val entry = Entry(set.entryCount.toFloat(), value)
        set.addEntry(entry)
        data.notifyDataChanged()


        chart.notifyDataSetChanged()
        chart.setVisibleXRangeMaximum(dataLimit.toFloat());
        chart.moveViewToX(data.entryCount.toFloat());
    }
}