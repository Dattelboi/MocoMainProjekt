package com.example.persistenceworkshopcodepreview.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.persistenceworkshopcodepreview.PomodoroViewModel
import com.example.persistenceworkshopcodepreview.TimerSettingViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    pomodoroViewModel: PomodoroViewModel,
    timerSettingViewModel: TimerSettingViewModel
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(pomodoroViewModel)
        }
        composable("settings") {
            SettingsScreen(timerSettingViewModel)
        }
    }
}
