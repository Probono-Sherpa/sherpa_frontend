package com.hansung.sherpa

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hansung.sherpa.convert.Convert
import com.hansung.sherpa.convert.LegRoute
import com.hansung.sherpa.geocoding.GeocodingAPI
import com.hansung.sherpa.transit.TransitManager
import com.hansung.sherpa.transit.TransitRouteRequest
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import java.lang.Thread.sleep

// 텍스트뷰에 값이 그래도 유지되고 있게하기 위한 뷰모델이다.
// searchRouteViewModel의 이름과 위치는 바꿀 필요가 있을듯
class SearchRouteViewModel:ViewModel() {
    val destinationText = MutableLiveData<String>()
}


class SearchRoute(val naverMap: NaverMap, val context: Context, val lifecycle: MainActivity) {
    // 출발지(내 위치)
    lateinit var departureLatLng:LatLng
    // 목적지는 값이 없을 수도 있다.
    var destinationLatLng:LatLng? = null

    // GeocodingAPI 클래스를 이용하여 원하는 주소의 좌표를 받아오는 함수이다.
    fun searchLatLng(destination:String): LatLng {
        val geoCodingApi = GeocodingAPI()
        geoCodingApi.request(destination,"목적지")

        // 여기서 무한루프 발생!!
        while(geoCodingApi.getFlag("목적지") == false) {
            sleep(100)
        }

        val coordinate = geoCodingApi.getCoordinate("목적지")
        val latLng = LatLng(coordinate!!.latitude, coordinate.longitude)
        return latLng
    }

    // 대중교통 이동 루트 검색 함수
    fun searchRoute() {
        val viewModel = SearchRouteViewModel()

        //val destinationText = viewModel.destinationText.value
        val destinationText = "116, Samseongyo-ro 16-gil, Seongbuk-gu, Seoul, Republic of Korea"

        destinationLatLng = searchLatLng(destinationText) // 오류 원인!!

        drawRoute()
    }

    // 정해진 루트에 선을 그리는 함수
    fun drawRoute() {
        // 출발지점은 현재 자신의 좌표값을 받아서 설정한다.
        departureLatLng = naverMap.locationOverlay.position

        Log.d("getLatLng", departureLatLng.longitude.toString())

        // 요청 param setting
        val routeRequest = TransitRouteRequest(
            //startX = "126.926493082645",
            startX = departureLatLng.longitude.toString(),
            //startY = "37.6134436427887",
            startY = departureLatLng.latitude.toString(),
            //endX = "127.126936754911",
            endX = destinationLatLng!!.longitude.toString(),
            //endY = "37.5004198786564",
            endY = destinationLatLng!!.latitude.toString(),
            lang = 0,
            format = "json",
            count = 1
        )

        var transitRoutes: MutableList<MutableList<LegRoute>>

        // 관찰 변수 변경 시 콜백
        TransitManager(this.context).getTransitRoutes(routeRequest).observe(this.lifecycle) { transitRouteResponse ->
            transitRoutes = Convert().convertToRouteMutableLists(transitRouteResponse)
            val tt = Convert().convertToSearchRouteDataClass(transitRoutes[0])

            val COORDS_1 = tt.map { it -> it.latLng }

            // pathOverlay는 네이버에서 제공하는 선 그리기 함수이며, 거기에 각 속성을 더 추가하여 색상을 칠했다.
            val pathOverlay = PathOverlay().also {
                it.coords = COORDS_1
                it.width = 10
                it.color = Color.BLUE
                it.outlineColor = Color.WHITE
                it.passedColor = Color.RED
                it.passedOutlineColor = Color.WHITE
                it.progress = 0.3
                it.map = this.naverMap
            }
        }
    }
}