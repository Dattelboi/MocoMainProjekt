package com.example.persistenceworkshopcodepreview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun TodoListScreen(navController: NavHostController, todoViewModel: TodoViewModel = viewModel()) {
    val todos = todoViewModel.allTodos.observeAsState(listOf())

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Todo List", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(todos.value) { todo ->
                TodoItem(todo) {
                    // On item click, navigate to home screen with todo title as parameter
                    navController.navigate("home/${todo.title}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("add_todo") }) {
            Text("Add Todo")
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = todo.title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (todo.isCompleted) "Completed" else "Not Completed",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
