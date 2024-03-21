package com.redbus.todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.redbus.todo.di.DBContainer
import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository
import com.redbus.todo.domain.usecases.DeleteTodoUseCase
import com.redbus.todo.domain.usecases.GetAllTodosUseCase
import com.redbus.todo.domain.usecases.GetByIdUseCase
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository,getall: GetAllTodosUseCase, val deleteTodoUseCase: DeleteTodoUseCase) : ViewModel() {

    val allTodo : LiveData<List<TodoItem>>

    init {


        allTodo = getall.execute()
    }
    fun insert(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.insert(todoItem)
        }
    }

    fun update(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.update(todoItem)
        }
    }
    fun delete(todoItem: TodoItem) {
        viewModelScope.launch {
            deleteTodoUseCase.execute(todoItem)
        }
    }

}


class StdVMFactory(val app: Application, val dbContainer: DBContainer): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = dbContainer.getDatabase(app)
        val repository = dbContainer.provideTodoRepository(db)
        val getall=GetAllTodosUseCase(repository)
        val deleteTodoUseCase=DeleteTodoUseCase(repository)
        return TodoViewModel(repository,getall,deleteTodoUseCase) as T
    }
}