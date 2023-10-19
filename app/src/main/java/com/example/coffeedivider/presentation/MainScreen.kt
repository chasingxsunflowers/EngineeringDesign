package com.example.coffeedivider.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.coffeedivider.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

@Composable
fun MainScreen(
    navController: NavController,
    dayOne: MutableState<String>,
    deadline: MutableState<String>,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.ChooseUserScreen.route)
                }

        ) {
            Image(
                painter = painterResource(R.drawable.arrow_left),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
            )
            Spacer(modifier = Modifier.width(270.dp))


        }
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "Make your coffee",
            fontSize = 30.sp,
            color = Color(0xFFC89B78),
            lineHeight = 42.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(20.dp)
        )

        Row(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.DialogScreen.route)
                }

        ) {
            Image(
                painter = painterResource(R.drawable.coffee),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        Row {
            Text(
                text = "Your progress:",
                fontSize = 23.sp,
                lineHeight = 42.sp,
                color = Color(0xFFC89B78),
                textAlign = TextAlign.Left,
                modifier = modifier
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.width(130.dp))
        }
        Spacer(modifier = Modifier.height(5.dp))

        //val day = 5.0f
        //val totalDays = 28.0f
        //(deadline.value).toFloat() * 7

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val firstDay = LocalDate.parse(dayOne.value, dateFormatter)
        val lastDay = LocalDate.parse(deadline.value, dateFormatter)
        val totalDays = (ChronoUnit.DAYS.between(firstDay, lastDay)).toFloat()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dateCurrent = LocalDate.of(year, month, dayOfMonth)
        val day = totalDays - (ChronoUnit.DAYS.between(dateCurrent, lastDay)).toFloat() + 1
        //Text(text = "First day: $firstDay , Last day: $lastDay , Current day: $dateCurrent , Day: $day , TotalDays: $totalDays")

        LinearProgressIndicator(
            progress = day / totalDays,
            color = Color(0xFFC89B78),
            trackColor = Color(0xFF80583E),
            modifier = Modifier
                .height(17.dp)
                .width(290.dp)
                .clip(RoundedCornerShape(16.dp))
        )


    }
}



