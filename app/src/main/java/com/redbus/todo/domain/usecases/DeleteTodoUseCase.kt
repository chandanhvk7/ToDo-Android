package com.redbus.todo.domain.usecases


import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository

class DeleteTodoUseCase(val repository: TodoRepository) {
    suspend fun execute(todoItem: TodoItem){
        repository.delete(todoItem)
    }
}