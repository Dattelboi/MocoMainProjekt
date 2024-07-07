package com.example.persistenceworkshopcodepreview

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.updateTodoStatus(todo.id, todo.isCompleted)
    }

    suspend fun delete(todo: Todo) {
        todoDao.deleteTodoById(todo.id)
    }
}
