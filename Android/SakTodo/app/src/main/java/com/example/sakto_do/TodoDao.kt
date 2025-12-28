package com.example.sakto_do

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao{
    @Query("SELECT * from todos")
    fun getAllTodos(): Flow<List<Todo>>

    // ✅ ADD THIS METHOD (FOR WIDGET USE)
    @Query("SELECT * FROM todos")
    suspend fun getAllTodosOnce(): List<Todo>

    @Insert
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)
}