package com.redbus.todo.model

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {
    val allTodoItems: LiveData<List<TodoItem>> = todoDao.getAllTodos()

    suspend fun insert(todoItem: TodoItem) {
        todoDao.insert(todoItem)
    }
    suspend fun delete(todo: TodoItem){
        todoDao.delete(todo)
    }
    suspend fun update(todo: TodoItem){
        todoDao.update(todo.id, todo.title,todo.isDone)
    }
}
