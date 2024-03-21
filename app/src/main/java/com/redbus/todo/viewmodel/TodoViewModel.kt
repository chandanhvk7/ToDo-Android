package com.redbus.todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.redbus.todo.di.DBContainer
import com.redbus.todo.domain.model.TodoItem
import com.redbus.todo.domain.repository.TodoRepository
import com.redbus.todo.domain.usecases.AddTodoUseCase
import com.redbus.todo.domain.usecases.DeleteTodoUseCase
import com.redbus.todo.domain.usecases.GetAllTodosUseCase
import com.redbus.todo.domain.usecases.GetByIdUseCase
import com.redbus.todo.domain.usecases.UpdateTodoUseCase
import kotlinx.coroutines.launch

class TodoViewModel(getall: GetAllTodosUseCase, val addTodoUseCase: AddTodoUseCase,val deleteTodoUseCase: DeleteTodoUseCase,val updateTodoUseCase: UpdateTodoUseCase) : ViewModel() {

    val allTodo : LiveData<List<TodoItem>>

    init {


        allTodo = getall.execute()
    }
    fun insert(todoItem: TodoItem) {
        viewModelScope.launch {
            addTodoUseCase.execute(todoItem)
        }
    }

    fun update(todoItem: TodoItem) {
        viewModelScope.launch {
            updateTodoUseCase.execute(todoItem)
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
        val updateTodoUseCase=UpdateTodoUseCase(repository)
        val addTodoUseCase=AddTodoUseCase(repository)
        return TodoViewModel(getall,addTodoUseCase,deleteTodoUseCase,updateTodoUseCase) as T
    }
}