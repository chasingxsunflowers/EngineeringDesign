package com.example.coffeedivider.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    onBluetoothStateChanged: () -> Unit
) {

    val navController = rememberNavController()
    val deadline = remember { mutableStateOf("") }
    val dayOne = remember { mutableStateOf("") }
    val cupSize = remember { mutableStateOf("") }


    NavHost(navController = navController, startDestination = Screen.CreatePlanScreen.route) {
        composable(Screen.StartScreen.route) {
            StartScreen(navController = navController)
        }

        composable(Screen.TemperatureHumidityScreen.route) {
            TemperatureHumidityScreen(navController = navController, onBluetoothStateChanged)
        }

        composable(Screen.MainScreen.route) {
            MainScreen(navController = navController, dayOne, deadline)
        }

        composable(Screen.CreatePlanScreen.route) {
            CreatePlanScreen(navController = navController, dayOne, deadline)
        }
        composable(Screen.ChooseUserScreen.route) {
            ChooseUserScreen(navController = navController)
        }

        dialog(Screen.DialogScreen.route) {
            DialogScreen(navController = navController, onDismissRequest = { null }, cupSize)
        }
    }
}

sealed class Screen(val route: String) {
    object StartScreen : Screen("start_screen")
    object TemperatureHumidityScreen : Screen("temp_humid_screen")
    object MainScreen : Screen("main_screen")
    object CreatePlanScreen : Screen("create_plan_screen")
    object ChooseUserScreen : Screen("choose_user_screen")
    object DialogScreen : Screen("dialog_screen")
}