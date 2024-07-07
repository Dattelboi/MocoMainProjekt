package com.example.persistenceworkshopcodepreview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("settings") {
            SettingsScreen(navController)
        }
        composable("todos") {
            TodoListScreen(navController)
        }
        composable("add_todo") {
            AddTodoScreen(navController)
        }
    }
}
