package com.redbus.todo.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
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

class AddViewmodel(val addTodoUseCase: AddTodoUseCase,val getByIdUseCase: GetByIdUseCase,val updateTodoUseCase: UpdateTodoUseCase): ViewModel() {
    val characterCount = MutableLiveData<Int>().apply { value = 0 }
    var todo:TodoItem?=null
    fun updateCharacterCount(title: String) {
        characterCount.value = title.length
    }
    fun update(todoItem: TodoItem) {
        viewModelScope.launch {
            updateTodoUseCase.execute(todoItem)
        }
    }

    fun getById(id:Int): TodoItem? {
        return getByIdUseCase.execute(id)

    }
    fun insert(todoItem: TodoItem) {
        viewModelScope.launch {
            addTodoUseCase.execute(todoItem)
        }
    }

}

class StdAddVMFactory(val app: Application, val dbContainer: DBContainer): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = dbContainer.getDatabase(app)
        val repository = dbContainer.provideTodoRepository(db)
        val addTodoUseCase= AddTodoUseCase(repository)
        val getByIdUseCase= GetByIdUseCase(repository)
        val updateTodoUseCase= UpdateTodoUseCase(repository)
        return AddViewmodel(addTodoUseCase,getByIdUseCase,updateTodoUseCase) as T
    }
}