package com.redbus.todo.domain.usecases

import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository

class GetByIdUseCase(val repository: TodoRepository) {
     fun execute(id:Int):TodoItem?{
        return repository.loadSingle(id)
    }
}