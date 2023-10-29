package com.example.coffeedivider.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coffeedivider.R

//The screen where the user can choose their profile
@Composable
fun ChooseUserScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        //Text "Who is making coffee?"
        Text(
            text = "Who is making coffee?",
            fontSize = 30.sp,
            lineHeight = 42.sp,
            color = Color(0xFFC89B78),
            textAlign = TextAlign.Center,
            modifier = modifier
                .padding(20.dp)
        )

        //The interactive user icon which leads them to their profile
        Box(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.MainScreen.route)
                }
        ) {
            Image(
                painter = painterResource(R.drawable.user),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.user),
            color = Color(0xFFC89B78),
            fontSize = 25.sp
        )
    }
}
