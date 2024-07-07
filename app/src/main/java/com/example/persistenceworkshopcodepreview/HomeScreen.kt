package com.example.persistenceworkshopcodepreview

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    todoViewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(LocalContext.current.applicationContext as Application)),
    timerViewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val todos by todoViewModel.allTodos.observeAsState(emptyList())
    val timerState by timerViewModel.timerState.observeAsState(TimerState(false, (timerViewModel.defaultTimerTime.value ?: 25) * 60 * 1000L, null))
    val defaultTimerTime by timerViewModel.defaultTimerTime.observeAsState(25)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Pomodoro Timer", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = if (timerState.isRunning) {
                "Time Left: ${timerState.timeLeft / 1000 / 60}:${(timerState.timeLeft / 1000 % 60).toString().padStart(2, '0')}"
            } else {
                "Default Time: ${defaultTimerTime}:00"
            },
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )

        Button(onClick = {
            scope.launch {
                if (timerState.isRunning) {
                    timerViewModel.stopTimer()
                } else {
                    timerViewModel.startTimer()
                }
            }
        }) {
            Text(text = if (timerState.isRunning) "Stop Timer" else "Start Timer")
        }

        Text(text = "Todo List", style = MaterialTheme.typography.headlineSmall)
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            items(todos) { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${todo.title} (${todo.duration} min)", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = {
                        scope.launch {
                            if (timerState.isRunning) {
                                timerViewModel.stopTimer()
                            } else {
                                timerViewModel.startTimer(todo.title, todo.duration)
                            }
                        }
                    }) {
                        Text(text = if (timerState.isRunning) "Stop Timer" else "Start Timer")
                    }
                }
            }
        }
    }
}
