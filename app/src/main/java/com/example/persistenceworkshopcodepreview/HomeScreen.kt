package com.example.persistenceworkshopcodepreview

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    val defaultTimerTime by timerViewModel.defaultTimerTime.observeAsState(initial = 25)
    val todos by todoViewModel.allTodos.observeAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var newTodoTitle by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Pomodoro Timer", style = MaterialTheme.typography.headlineMedium)
        Text(text = "${defaultTimerTime}:00", style = MaterialTheme.typography.displaySmall)

        Button(onClick = { scope.launch { timerViewModel.startTimer() } }) {
            Text(text = "Start Timer")
        }
        Button(onClick = { scope.launch { timerViewModel.stopTimer() } }) {
            Text(text = "Stop Timer")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            items(todos) { todo ->
                Text(text = todo.title, style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { scope.launch { timerViewModel.startTimer(todo.title) } }) {
                    Text(text = "Start ${todo.title}")
                }
            }
        }

        TextField(
            value = newTodoTitle,
            onValueChange = { newTodoTitle = it },
            label = { Text("Todo Title") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Button(onClick = {
            if (newTodoTitle.isNotBlank()) {
                todoViewModel.insertTodo(Todo(title = newTodoTitle, isCompleted = false))
                newTodoTitle = ""
            }
        }) {
            Text(text = "Add Todo")
        }
    }
}
