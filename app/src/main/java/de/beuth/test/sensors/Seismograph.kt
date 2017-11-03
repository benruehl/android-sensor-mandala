package de.beuth.test.sensors

import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet



/**
 * Created by User on 22.10.2017.
 */
class Seismograph (xChart : LineChart, yChart : LineChart, zChart : LineChart) {

    private val chartX : LineChart by lazy {xChart}
    private val chartY : LineChart by lazy {yChart}
    private val chartZ : LineChart by lazy {zChart}

    private var sensorDataX: MutableList<Entry> = mutableListOf()
    private var sensorDataY: MutableList<Entry> = mutableListOf()
    private var sensorDataZ: MutableList<Entry> = mutableListOf()

    private val dataLimit : Int = 30

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

    fun addData (valueX : Float, valueY : Float, valueZ : Float) {

        refreshChartData(chartX, valueX)
        refreshChartData(chartY, valueY)
        refreshChartData(chartZ, valueZ)

    }

    fun setupChart(chart : LineChart) {
        val xAxis : XAxis = chart.xAxis
        xAxis.setEnabled(false)

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

    fun createDateSet() : LineDataSet {
        val dataSet : LineDataSet = LineDataSet(null, "SensorData")
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawHighlightIndicators(false)
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
        chart.setVisibleXRangeMaximum(20f);
        chart.moveViewToX(data.entryCount.toFloat());
    }
}