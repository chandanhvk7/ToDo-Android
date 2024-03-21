package com.redbus.todo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.redbus.todo.domain.model.TodoItem

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoItem)

    @Delete
    suspend fun delete(todo: TodoItem)

    @Query("SELECT * from todo_items order by id ASC")
    fun getAllTodos(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE id=:id ")
    fun loadSingle(id: String): TodoItem

    @Query("UPDATE todo_items set title = :title,isDone= :isDone where id = :id")
    suspend fun update(id: Int?, title: String?,isDone:Boolean?)
}