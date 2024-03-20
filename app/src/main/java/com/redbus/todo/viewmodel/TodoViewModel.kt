package com.redbus.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.redbus.todo.model.TodoDatabase
import com.redbus.todo.model.TodoItem
import com.redbus.todo.model.TodoRepository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : ViewModel() {
    private val repository: TodoRepository
    val allTodo : LiveData<List<TodoItem>>

    init {
        val dao = TodoDatabase.getDatabase(application).getTodoDao()
        repository = TodoRepository(dao)
        allTodo = repository.allTodoItems
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
            repository.delete(todoItem)
        }
    }

}


class StdVMFactory(val app: Application): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodoViewModel(app) as T
    }
}