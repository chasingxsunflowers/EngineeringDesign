package com.example.coffeedivider.presentation

import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeedivider.R
import java.time.LocalDate
import java.time.Period
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun CreatePlanScreen(
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
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Let's create your plan!",
            fontSize = 26.sp,
            color = Color(0xFFC89B78),
            lineHeight = 42.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(20.dp)
        )

        Spacer(modifier = Modifier.height(60.dp))

        Row {
            Text(
                text = "Select the date by which you want to reach 0% decaf:",
                fontSize = 18.sp,
                lineHeight = 25.sp,
                color = Color(0xFFC89B78),
                textAlign = TextAlign.Left,
                modifier = modifier
                    .padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.width(200.dp))

        }
        Spacer(modifier = Modifier.height(5.dp))

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val date = LocalDate.of(year, month, dayOfMonth)
        dayOne.value = date.toString()

        val periodWeek3 = Period.of(0, 0, 21)
        val periodWeek4 = Period.of(0, 0, 28)
        val periodWeek5 = Period.of(0, 0, 35)
        val periodWeek6 = Period.of(0, 0, 42)

        val week3 = date.plus(periodWeek3)
        val week4 = date.plus(periodWeek4)
        val week5 = date.plus(periodWeek5)
        val week6 = date.plus(periodWeek6)


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val isSelectedItem: (String) -> Boolean = { deadline.value == it }
            val onChangeState: (String) -> Unit = { deadline.value = it }

            val items = listOf("$week3", "$week4", "$week5", "$week6")


            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = isSelectedItem(item),
                            onClick = { onChangeState(item) },
                            role = Role.RadioButton
                        )
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.width(30.dp))
                    RadioButton(
                        selected = isSelectedItem(item),
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFC89B78),
                            unselectedColor = Color(0xFFC89B78),
                            disabledSelectedColor = Color(0xFFC89B78),
                            disabledUnselectedColor = Color(0xFFC89B78)
                        ),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = item,
                        fontSize = 18.sp,
                        color = Color(0xFFC89B78),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

//            Row { //this will be shown on the next page
//                Text(
//                    text = "Selected date: ${deadline.value.ifEmpty { "NONE" }} (TESTING)",
//                    fontSize = 18.sp,
//                    lineHeight = 25.sp,
//                    color = Color(0xFFC89B78),
//                    textAlign = TextAlign.Left,
//                    modifier = modifier
//                        .padding(horizontal = 30.dp)
//                )
//
//                Spacer(modifier = Modifier.width(200.dp))
//
//            }

            Spacer(modifier = Modifier.height(40.dp))

            Row {
                Text(
                    text = "How many cups do you drink per day?",
                    fontSize = 18.sp,
                    lineHeight = 25.sp,
                    color = Color(0xFFC89B78),
                    textAlign = TextAlign.Left,
                    modifier = modifier
                        .padding(horizontal = 30.dp)
                )

                Spacer(modifier = Modifier.width(200.dp))

            }



            Spacer(modifier = Modifier.height(15.dp))

            var numberOfCups by remember { mutableStateOf("2") }

            OutlinedTextField(
                value = numberOfCups,
                onValueChange = { numberOfCups = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFC89B78),
                    unfocusedBorderColor = Color(0xFFC89B78),
                    textColor = Color(0xFFC89B78),
                    cursorColor = Color(0xFFC89B78)
                )
            )

//            Row { //this will be shown on the next page
//                Text(
//                    text = "Amount of cups: $numberOfCups (TESTING)",
//                    fontSize = 18.sp,
//                    lineHeight = 25.sp,
//                    color = Color(0xFFC89B78),
//                    textAlign = TextAlign.Left,
//                    modifier = modifier
//                        .padding(horizontal = 30.dp)
//                )
//
//                Spacer(modifier = Modifier.width(200.dp))
//
//            }


            Spacer(modifier = Modifier.height(70.dp))

            Row(
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screen.ChooseUserScreen.route)
                    }
            ) {
                Spacer(modifier = Modifier.width(260.dp))

                Image(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
    }
}
