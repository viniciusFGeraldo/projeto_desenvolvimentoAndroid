package com.example.sgos.model.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgos.model.entity.Cliente

interface ClienteDao {

    @Insert
    suspend fun inserirCliente(cliente: Cliente)

    @Query("SELECT * FROM cliente")
    suspend fun buscarTodos() : List<Cliente>

    @Delete
    suspend fun deletar(cliente: Cliente)

    @Update
    suspend fun atualizar(cliente: Cliente)
}