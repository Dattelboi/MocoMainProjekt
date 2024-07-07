package com.example.persistenceworkshopcodepreview.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.persistenceworkshopcodepreview.TimerSettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(timerSettingViewModel: TimerSettingViewModel) {
    var defaultTimerTime by remember { mutableStateOf(TextFieldValue("25")) }

    LaunchedEffect(Unit) {
        timerSettingViewModel.getTimerSetting { setting ->
            defaultTimerTime = TextFieldValue(setting?.defaultTimerTime?.toString() ?: "25")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Settings", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = defaultTimerTime,
            onValueChange = { defaultTimerTime = it },
            label = { Text("Default Timer Time (minutes)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val newTime = defaultTimerTime.text.toIntOrNull() ?: 25
                timerSettingViewModel.updateTimerSetting(newTime)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
