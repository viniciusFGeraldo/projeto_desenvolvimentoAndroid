package com.example.sgos.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgos.model.entity.OrdemServico
@Dao
interface OrdemServicoDao {

    @Insert
    suspend fun inserirOrdemServico(ordemServico: OrdemServico)

    @Query("SELECT * FROM ordemServico")
    suspend fun buscarTodos() : List<OrdemServico>

    @Delete
    suspend fun deletar(ordemServico: OrdemServico)

    @Update
    suspend fun atualizar(ordemServico: OrdemServico)
}