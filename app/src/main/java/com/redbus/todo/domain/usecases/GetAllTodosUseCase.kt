package com.redbus.todo.domain.usecases

import androidx.lifecycle.LiveData
import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository

class GetAllTodosUseCase(val repository: TodoRepository) {

    fun execute():LiveData<List<TodoItem>> {
        return repository.allTodoItems()
    }
}