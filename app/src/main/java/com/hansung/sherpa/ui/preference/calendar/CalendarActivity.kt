package com.hansung.sherpa.ui.preference.calendar

//import org.threeten.bp.DayOfWeek
//import org.threeten.bp.LocalDate
//import org.threeten.bp.YearMonth
//import org.threeten.bp.format.DateTimeFormatter
//import org.threeten.bp.format.TextStyle
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class CalendarActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this);
        setContent {
            CalendarScreen{
                finish()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    finish : () -> Unit
){
    var scheduleDataList = remember { mutableStateListOf<ScheduleData>() }
    var showBottomSheet by remember { mutableStateOf(false) }
    val beforeSelectedLocalDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    val closeBottomSheet : (ScheduleData, Boolean) -> Unit = { item, isAdded ->
        if(isAdded){
            // TODO: 추가 API 호출
            scheduleDataList.add(item)
        }
        showBottomSheet = false
    }
    val updateScheduleData : @Composable (LocalDate) -> Unit = { changeDate ->
        // TODO: 조회 API 호출
        if(!changeDate.isEqual(beforeSelectedLocalDate))
            scheduleDataList.clear()
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = Color.DarkGray
                ),

                title = {
                    Text(
                        text = "캘린더",
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
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(shape = RoundedCornerShape(40.dp))
                    .width(80.dp)
                    .height(40.dp),
                containerColor = Color.Black,
                contentColor = Color.White,
            ) {
                Text(text = "추가하기")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerpadding ->
            Surface(modifier = Modifier.padding(innerpadding)) {
                Calendar(scheduleDataList = scheduleDataList, updateScheduleData = updateScheduleData)
            }
        }
    )

    if (showBottomSheet){
        val startDateTime = android.icu.util.Calendar.getInstance().apply {
            set(android.icu.util.Calendar.MINUTE, 0)
        }
        val endDateTime = android.icu.util.Calendar.getInstance().apply {
            set(android.icu.util.Calendar.MINUTE, 0)
        }
        val scheduleData = ScheduleData(
            title = remember { mutableStateOf("") },
            scheduledLocation = remember {
                ScheduleLocation(
                    "",
                    "",
                    0.0,
                    0.0
                )
            },
            isWholeDay = remember { mutableStateOf(false) },
            isDateValidate = remember { mutableStateOf(true )},
            startDateTime = remember { mutableLongStateOf(startDateTime.timeInMillis) },
            endDateTime = remember { mutableLongStateOf(endDateTime.timeInMillis) },
            repeat = remember { mutableStateOf(Repeat(repeatable = false, cycle = "")) },
            comment = remember { mutableStateOf("") }
        )
        ScheduleBottomSheet(
            closeBottomSheet = closeBottomSheet,
            scheduleData = scheduleData,
            scheduleModalSheetOption = ScheduleModalSheetOption.ADD
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(
    scheduleDataList : SnapshotStateList<ScheduleData>,
    config: CalendarConfig = CalendarConfig(),
    currentDate: LocalDate = LocalDate.now(),
    updateScheduleData: @Composable (LocalDate) -> Unit
) {
    val initialPage = (currentDate.year - config.yearRange.first) * 12 + currentDate.monthValue - 1
    var currentSelectedDate by remember { mutableStateOf(currentDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var currentPage by remember { mutableIntStateOf(initialPage) }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { (config.yearRange.last - config.yearRange.first + 1) * 12 }
    )
    val coroutineScope = rememberCoroutineScope()
    updateScheduleData(currentSelectedDate)

//    LaunchedEffect(pagerState.currentPage) {
//        val addMonth = (pagerState.currentPage - currentPage).toLong()
//        currentMonth = currentMonth.plusMonths(addMonth)
//        currentPage = pagerState.currentPage
//    }

    val onChangeMonth : @Composable (Int) -> Unit = { offset ->
        with(pagerState) {
            LaunchedEffect(key1 = currentPage) {
                coroutineScope.launch {
                    animateScrollToPage(
                        page = (currentPage + offset).mod(pageCount)
                    )
                }
                currentMonth = currentMonth.plusMonths(offset.toLong())
                currentPage = pagerState.currentPage
                val yearMonth = YearMonth.now()
                currentSelectedDate = when ((currentMonth.month == yearMonth.month && currentMonth.year == yearMonth.year)){
                    true -> LocalDate.now()
                    false -> LocalDate.of(currentMonth.year, currentMonth.month, 1)
                }
            }
        }
    }

    LazyColumn(modifier = Modifier) {
        val headerText = currentMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월"))
        item{
            CalendarHeader(
                onChangeMonth = onChangeMonth,
                text = headerText,
            )
        }
        item {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 700.dp)
                    .wrapContentSize()
                    .padding(bottom = 18.dp)
            ) { page ->
                val date = LocalDate.of(
                    config.yearRange.first + page / 12,
                    (page % 12) + 1,
                    1
                )
                CalendarMonthItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    currentDate = date,
                    selectedDate = currentSelectedDate,
                    onSelectedDate = { date ->
                        currentSelectedDate = date
                    }
                )
            }
        }
        item{
            CurrentDateColumn(currentSelectedDate)
            ScheduleColumns(scheduleDataList = scheduleDataList)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarMonthItem(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onSelectedDate: (LocalDate) -> Unit
) {
    val lastDay = currentDate.lengthOfMonth()
    val firstDayOfWeek = currentDate.withDayOfMonth(1).dayOfWeek.value % 7
    val days = IntRange(1, lastDay).toList()

    Column(modifier = modifier) {
        DayOfWeek()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7)
        ) {
            for (i in 0 until firstDayOfWeek) {
                item {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 10.dp)
                    )
                }
            }
            items(days) { day ->
                val date = currentDate.withDayOfMonth(day)
                val isSelected = remember(selectedDate) {
                    selectedDate.compareTo(date) == 0
                }
                CalendarDay(
                    modifier = Modifier.padding(top = 10.dp),
                    date = date,
                    isToday = date == LocalDate.now(),
                    isSelected = isSelected,
                    onSelectedDate = onSelectedDate
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDay(
    modifier: Modifier = Modifier,
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    hasEvent: Boolean = false,
    onSelectedDate: (LocalDate) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .size(30.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(
                color = when {
                    isSelected -> Color.Black
                    isToday -> Color.Gray
                    else -> Color(234, 232, 239)
                }
            )
            .indication(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(4.dp)
            .clickable { onSelectedDate(date) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textColor = if (isSelected || isToday) Color.White else
            when(date.dayOfWeek){
                DayOfWeek.SUNDAY -> Color.Red
                DayOfWeek.SATURDAY -> Color.Blue
                else -> Color.Black
            }

        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = date.dayOfMonth.toString(),
            color = textColor
        )
        if (hasEvent) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .let {
                        if (isSelected || isToday)
                            it.background(Color.White)
                        else
                            it.background(Color.Black)
                    }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayOfWeek(
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        val daysOfWeek = listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = when(dayOfWeek){
                    DayOfWeek.SUNDAY -> Color.Red
                    DayOfWeek.SATURDAY -> Color.Blue
                    else -> Color.Black
                },
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarHeader(
    onChangeMonth : @Composable (Int) -> Unit,
    text: String,
) {
    var isLeftArrowClick by remember { mutableStateOf(false) }
    var isRightArrowClick by remember { mutableStateOf(false) }

    if(isLeftArrowClick){
        isLeftArrowClick = false
        onChangeMonth(-1)
    }
    if(isRightArrowClick){
        isRightArrowClick = false
        onChangeMonth(1)
    }

    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = { isLeftArrowClick = true },
            modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "이전달")
        }
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        IconButton(
            onClick = { isRightArrowClick = true },
            modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "이전달")
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewCalendarPreference() {
    CalendarScreen{}
}