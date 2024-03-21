package com.redbus.todo.di

import android.content.Context
import androidx.room.Room
import com.redbus.todo.data.TodoDatabase
import com.redbus.todo.domain.repository.TodoRepository

class DBContainer {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    TodoDatabase.DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    fun provideTodoRepository(database: TodoDatabase):TodoRepository=TodoRepository.getInstance(
        database.getTodoDao()
    )
}

