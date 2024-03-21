package com.redbus.todo.domain.repository

import androidx.lifecycle.LiveData
import com.redbus.todo.domain.model.TodoItem

//class TodoRepository(private val todoDao: TodoDao) {
//    val allTodoItems: LiveData<List<TodoItem>> = todoDao.getAllTodos()
//
//    suspend fun insert(todoItem: TodoItem) {
//        todoDao.insert(todoItem)
//    }
//    suspend fun delete(todo: TodoItem){
//        todoDao.delete(todo)
//    }
//    suspend fun update(todo: TodoItem){
//        todoDao.update(todo.id, todo.title,todo.isDone)
//    }
//}

interface Repository {
    fun allTodoItems():LiveData<List<TodoItem>>
    suspend fun insert(todoItem: TodoItem)
    suspend fun delete(todo: TodoItem)
    suspend fun update(todo: TodoItem)
     fun loadSingle(id: Int):TodoItem?
}
