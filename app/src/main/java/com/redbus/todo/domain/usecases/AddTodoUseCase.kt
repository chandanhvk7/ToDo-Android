package com.redbus.todo.domain.usecases

import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository

class AddTodoUseCase(val repository: TodoRepository) {
    suspend fun execute(todoItem: TodoItem){
        repository.insert(todoItem)
    }
}