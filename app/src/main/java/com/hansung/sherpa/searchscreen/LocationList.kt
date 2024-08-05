package com.hansung.sherpa.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintSet.Layout
import com.hansung.sherpa.itemsetting.LatLng
import com.hansung.sherpa.searchlocation.SearchLoactionCallBack
import com.hansung.sherpa.searchlocation.SearchLocation
import com.hansung.sherpa.searchlocation.SearchLocationResponse

/**
 * 해야할 것
 * 1. 끝날 때 locationValue가 0이 되어야한다.
 * 2. expand flag가 false가 되어야 한다.
 */
@Composable
fun LocationList(locationValue:String, searchLocation: (String) -> Unit ,update: (String, LatLng) -> Unit) {
    var searchLocationResponse by remember { mutableStateOf(SearchLocationResponse()) }

    LaunchedEffect(locationValue) {
        SearchLocation().search(locationValue, object : SearchLoactionCallBack {
            override fun onSuccess(response: SearchLocationResponse?) {
                searchLocationResponse = response!!
            }

            override fun onFailure(message: String) {}
        })
        searchLocation(locationValue)
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        items(searchLocationResponse.items){
            Row(modifier = Modifier.background(Color.White).padding(vertical = 8.dp).clickable {
                // 경도 (lon)
                val x = it.mapx?.toDouble()?.div(10000000)!!

                // 위도 (lat)
                val y = it.mapy?.toDouble()?.div(10000000)!!

                update(it.title?:"Null",LatLng(y,x))

                searchLocationResponse = SearchLocationResponse()
            }){
                Text(text = it.title!! , maxLines = 1)

                Spacer(modifier = Modifier.weight(1f))

                Text(text = it.address!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}