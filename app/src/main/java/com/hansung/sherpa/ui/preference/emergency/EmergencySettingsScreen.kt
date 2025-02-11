package com.hansung.sherpa.ui.preference.emergency

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.jakewharton.threetenabp.AndroidThreeTen

class EmergencySettingsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this);
        setContent {
            EmergencySettingsScreen {
                finish()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencySettingsScreen(
    finish : () -> Unit
){
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = Color.DarkGray
                ),

                title = {
                    Text(
                        text = "긴급 연락처",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Cursive,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { finish() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "뒤로가기")
                    }
                },
            )
        },
        content = { innerpadding ->
            Surface(modifier = Modifier.padding(innerpadding)) {
                EmergencySettingsArea {
                    finish()
                }
            }
        }
    )
}
@Preview
@Composable
fun EmergencySettingsPreview() {
    EmergencySettingsScreen({})
}