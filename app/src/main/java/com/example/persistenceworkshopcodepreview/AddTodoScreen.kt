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
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    navController: NavHostController,
    timerSettingViewModel: TimerSettingViewModel = viewModel(factory = TimerSettingViewModelFactory(LocalContext.current.applicationContext as Application)),
    todoViewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    var title by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    val defaultTimerTime by timerSettingViewModel.defaultTimerTime.observeAsState(initial = 25)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Todo Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(onClick = {
            if (title.isNotBlank()) {
                val todoDuration = duration.toIntOrNull() ?: defaultTimerTime
                todoViewModel.insertTodo(Todo(title = title, duration = todoDuration, isCompleted = false))
                navController.navigate("todos") // Navigate back to the todo list after adding
            }
        }) {
            Text("Add Todo")
        }
    }
}
