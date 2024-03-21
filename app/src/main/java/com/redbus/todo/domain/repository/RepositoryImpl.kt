package com.redbus.todo.domain.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.redbus.todo.data.TodoDao
import com.redbus.todo.di.DBContainer
import com.redbus.todo.domain.model.TodoItem

//object RepositoryContainer {
//    @Volatile
//    private var INSTANCE: Repository? = null
//
//    fun getRepository(context: Context): Repository {
//        return INSTANCE ?: synchronized(this) {
//            val database = DBContainer.getDatabase(context)
//            val instance = TodoRepository(database.todoDao())
//            INSTANCE = instance
//            instance
//        }
//    }
//}

class TodoRepository private constructor(private val todoDao: TodoDao) : Repository {
    override fun allTodoItems(): LiveData<List<TodoItem>> {
        return todoDao.getAllTodos()
    }

    override suspend fun insert(todoItem: TodoItem) {
        todoDao.insert(todoItem)
    }

    override suspend fun delete(todo: TodoItem) {
        todoDao.delete(todo)
    }

    override suspend fun update(todo: TodoItem) {
        todoDao.update(todo.id, todo.title, todo.isDone)
    }

    override fun loadSingle(id:String): TodoItem {
        return todoDao.loadSingle(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: TodoRepository? = null

        fun getInstance(todoDao: TodoDao): TodoRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = TodoRepository(todoDao)
                INSTANCE = instance
                instance
            }
        }
    }
}
