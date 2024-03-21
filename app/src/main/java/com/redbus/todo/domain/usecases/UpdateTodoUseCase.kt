package com.redbus.todo.domain.usecases

import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository

class UpdateTodoUseCase(val repository: TodoRepository) {
    suspend fun execute(todoItem: TodoItem){
        repository.update(todoItem)
    }
}