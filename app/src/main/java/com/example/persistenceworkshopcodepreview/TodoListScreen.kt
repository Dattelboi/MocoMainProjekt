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
fun TodoListScreen(
    navController: NavHostController,
    todoViewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val todos by todoViewModel.allTodos.observeAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Todo List", style = MaterialTheme.typography.headlineMedium)

        var title by remember { mutableStateOf("") }

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Todo Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(onClick = {
            if (title.isNotBlank()) {
                todoViewModel.insertTodo(Todo(title = title, isCompleted = false))
                title = ""
            }
        }) {
            Text("Add Todo")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            items(todos) { todo ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = todo.title, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { scope.launch { todoViewModel.delete(todo) } }) {
                        Text(text = "Delete")
                    }
                }
            }
        }
    }
}
