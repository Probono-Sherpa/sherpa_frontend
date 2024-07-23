package com.hansung.sherpa.itemsetting

import android.graphics.Color
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.hansung.sherpa.R
import com.hansung.sherpa.StaticValue

class ShowSpecificRoute {

    /*TODO 생성자로 전달받기 or 메소드 이용하기*/

    private var showRouteDetails:MutableList<RouteDetailItem> = mutableListOf(
        RouteDetailItem("한성대공학관", "한성대학교 정문", "도보150m", "2분"),
        RouteDetailItem("한성대학교정문", "한성대입구역, 성북02", "6개정류장", "8분"),
        RouteDetailItem("한성대입구역, 성북02", "한성대입구역2번출구", "도보84m", "2분")
    )//임시 변수 이후 삭제 -> 선택된 값을 위와 같이 작성하여 전달
    fun showSpecificRoute(){
        var adapter = RouteDetailAdapter(showRouteDetails, StaticValue.mainActivity)
        val specificRoute = StaticValue.mainActivity.findViewById<RecyclerView>(R.id.specific_route)
        specificRoute.adapter = adapter
        specificRoute.layoutManager = LinearLayoutManager(StaticValue.mainActivity)

        StaticValue.mainActivity.findViewById<ConstraintLayout>(R.id.mainwrapper).visibility = View.GONE


        val sbc = StaticValue.mainActivity.findViewById<HorizontalBarChart>(R.id.specificrouteprogress) //Stacked Bar Chart
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, floatArrayOf(10f, 80f, 10f)))

        val set = BarDataSet(entries, "")
        set.colors = mutableListOf(
            Color.GRAY,
            Color.GREEN,
            Color.GRAY
        )
        val data = BarData(set)
        data.setDrawValues(false)
        data.isHighlightEnabled = false
        sbc.data = data

        sbc.axisLeft.axisMinimum = 0f //좌우 최소
        sbc.axisLeft.axisMaximum = 100f //좌우 최대

        sbc.xAxis.axisMaximum = 1.5f // 두께 작을수록 두꺼워짐

        sbc.axisLeft.setDrawGridLines(false)
        sbc.axisLeft.setDrawAxisLine(false)
        sbc.axisLeft.setDrawLabels(false)

        sbc.xAxis.setDrawGridLines(false)
        sbc.xAxis.setDrawAxisLine(false)
        sbc.xAxis.setDrawLabels(false)

        sbc.axisRight.setDrawAxisLine(false)
        sbc.axisRight.setDrawGridLines(false)
        sbc.axisRight.setDrawLabels(false)

        sbc.description.isEnabled = false
        sbc.legend.isEnabled = false
        sbc.invalidate()


        val specificCoordinatorLayout = StaticValue.mainActivity.findViewById<CoordinatorLayout>(R.id.specific_route_coordinatorlayout)
        specificCoordinatorLayout.visibility = View.VISIBLE
    }

}