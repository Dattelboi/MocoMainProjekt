package com.example.persistenceworkshopcodepreview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.persistenceworkshopcodepreview.ui.theme.PersistenceWorkshopTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    private lateinit var timerViewModel: TimerViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        lifecycle.addObserver(TimerLifecycleObserver(timerViewModel))

        setContent {
            PersistenceWorkshopTheme {
                // Set system bar colors
                SetSystemBarColors()

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController)
                        }
                    ) { innerPadding ->
                        Navigation(navController, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun SetSystemBarColors() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color(0xFF32547a), // Blau mit Grau Anteil
            darkIcons = useDarkIcons
        )
    }
}
