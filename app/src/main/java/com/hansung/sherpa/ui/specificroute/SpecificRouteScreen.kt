package com.hansung.sherpa.ui.specificroute


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.hansung.sherpa.itemsetting.BusLane
import com.hansung.sherpa.itemsetting.BusSectionInfo
import com.hansung.sherpa.itemsetting.PedestrianSectionInfo
import com.hansung.sherpa.itemsetting.SectionInfo
import kotlin.math.roundToInt

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SpecificRouteScreen(){
    var showRouteDetails:MutableList<SectionInfo> = mutableListOf(
        PedestrianSectionInfo(200.0, 20, "한성대공학관", "한성대학교 정문",0.0,0.0,0.0,0.0,mutableListOf("200m 직진후 횡단보도", "500m 우회전", "50m 앞 공사현장")),
        BusSectionInfo(1600.0, 30, "한성대학교정문", "한성대입구역",0.0,0.0,0.0,0.0, listOf(BusLane("","성북02",0,0,"0",0)), 6, 0,0,0,"null",0,0,0,"null",mutableListOf("한성대입구역", "화정역", "은평구", "어쩌구 저쩌구", "등등")),
        PedestrianSectionInfo(200.0, 5, "한성대입구역", "한성대입구역2번출구",0.0,0.0,0.0,0.0,mutableListOf("200m 직진", "500m 우회전","200m 좌회전", "500m 로롤","200m 직진", "500m 우회전"))
    )

    val progress by remember { mutableStateOf(0.5f) }

    val swipeableState = rememberSwipeableState(0)

    val movedSize = 500.dp // 얼마 만큼 움직일 것인가 (거리 정의)
    val movingPx = with(LocalDensity.current) { movedSize.toPx() }
    val anchors = mapOf(0f to 0, movingPx to 1)


    Spacer(modifier = Modifier.fillMaxWidth().height(250.dp))


    Box(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.8f) },
                orientation = Orientation.Vertical
            )
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.Green)
                .wrapContentSize()
                .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) },
            colors = cardColors(
                containerColor = Color.LightGray  // 카드의 배경 색상 설정
            ),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
            border = BorderStroke(2.dp, Color.Black)
        ){
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                SpecificPreview(progress)
//            Canvas(// 선
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                drawLine(
//                    start = Offset(0f, size.height / 2),
//                    end = Offset(size.width, size.height / 2),
//                    color = Color.Red,
//                    strokeWidth = 7f
//                )
//            }
                SpecificList(showRouteDetails)
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun SpecificRoutePreview(){
    SpecificRouteScreen()
}