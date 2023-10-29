package com.example.coffeedivider.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeedivider.BLEApplication.Companion.globalVar
import com.example.coffeedivider.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

//The user's personalized screen
@Composable
fun MainScreen(
    navController: NavController,
    dayOne: MutableState<String>,
    deadline: MutableState<String>,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        //Interactive arrow which leads to the ChooseUserScreen.kt screen
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

        //Text "Make your coffee"
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

        //Coffee mug button which opens the "make your coffee" dialog
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

        //Text "Your progress"
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

        //Calculations behind the functionality of the progress bar

        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val firstDay = LocalDate.parse(dayOne.value, dateFormatter)
        val lastDay = LocalDate.parse(deadline.value, dateFormatter)
        val totalDays = (ChronoUnit.DAYS.between(firstDay, lastDay)).toFloat()
        val weeks = totalDays/7

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val dateCurrent = LocalDate.of(year, month, dayOfMonth)
        val day = totalDays - (ChronoUnit.DAYS.between(dateCurrent, lastDay)).toFloat() + 1

        globalVar = weeks.toInt().toString() + day.toInt().toString()

        //The personalized progress bar - it's state depends on the current day and the chosen by the user number of weeks
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



