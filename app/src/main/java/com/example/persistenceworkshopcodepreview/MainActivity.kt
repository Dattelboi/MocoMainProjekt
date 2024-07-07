package com.example.persistenceworkshopcodepreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.persistenceworkshopcodepreview.ui.Navigation
import com.example.persistenceworkshopcodepreview.ui.theme.PersistenceWorkshopTheme
import com.example.persistenceworkshopcodepreview.ui.BottomNavigationBar

class MainActivity : ComponentActivity() {

    private val pomodoroViewModel: PomodoroViewModel by viewModels()
    private val timerSettingViewModel: TimerSettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersistenceWorkshopTheme {
                MainScreen(pomodoroViewModel, timerSettingViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(pomodoroViewModel: PomodoroViewModel, timerSettingViewModel: TimerSettingViewModel) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pomodoro Timer") })
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Navigation(navController = navController, pomodoroViewModel = pomodoroViewModel, timerSettingViewModel = timerSettingViewModel)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PersistenceWorkshopTheme {
        MainScreen(pomodoroViewModel = viewModel(), timerSettingViewModel = viewModel())
    }
}
