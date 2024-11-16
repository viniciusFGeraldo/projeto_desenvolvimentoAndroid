package com.example.sgos.model.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgos.model.entity.Equipamento

interface EquipamentoDao {
    @Insert
    suspend fun inserirEquipamento(equipamento: Equipamento)

    @Query("SELECT * FROM equipamento")
    suspend fun buscarTodos() : List<Equipamento>

    @Delete
    suspend fun deletar(equipamento: Equipamento)

    @Update
    suspend fun atualizar(equipamento: Equipamento)
}