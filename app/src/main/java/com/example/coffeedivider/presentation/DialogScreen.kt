package com.example.coffeedivider.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController

//The dialog which shows up when the user clicks on the coffee mug button
@Composable
fun DialogScreen(
    navController: NavController,
    onDismissRequest: () -> Unit,
    cupSize: MutableState<String>
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF80583E),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val isSelectedItem: (String) -> Boolean = { cupSize.value == it }
                val onChangeState: (String) -> Unit = { cupSize.value = it }

                val items = listOf("Small", "Medium", "Large")

                //List with the cup size options that the user can choose
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
                            color = Color(0xFFC89B78),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                //"Make your coffee" button which initializes the bluetooth connection
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(40.dp)
                        .width(200.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF593117), CircleShape)
                        .clickable {
                            navController.navigate(Screen.BluetoothScreen.route)
                        }
                ){
                    Text(
                        text = "Make your coffee",
                        color = Color(0xFFC89B78))
                }
            }
        }
    }
}
