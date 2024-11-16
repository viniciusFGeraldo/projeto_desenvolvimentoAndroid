package com.example.sgos.model.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgos.model.entity.Acabamento

interface AcabamentoDao {

    @Insert
    suspend fun inserirAcabamento(acabamento: Acabamento)

    @Query("SELECT * FROM acabamento")
    suspend fun buscarTodos() : List<Acabamento>

    @Delete
    suspend fun deletar(acabamento: Acabamento)

    @Update
    suspend fun atualizar(acabamento: Acabamento)

}