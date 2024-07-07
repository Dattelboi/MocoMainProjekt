package com.example.persistenceworkshopcodepreview

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    timerSettingViewModel: TimerSettingViewModel = viewModel(factory = TimerSettingViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    var newTimerTime by remember { mutableStateOf("") }
    val context = LocalContext.current

    val currentTimerTime by timerSettingViewModel.defaultTimerTime.observeAsState(initial = 25)

    LaunchedEffect(currentTimerTime) {
        newTimerTime = currentTimerTime.toString()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = newTimerTime,
            onValueChange = { newTimerTime = it },
            label = { Text("Default Timer Time") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val time = newTimerTime.toIntOrNull() ?: 25
            timerSettingViewModel.updateTimerSetting(time)
        }) {
            Text("Save")
        }
    }
}
