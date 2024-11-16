package com.example.sgos.model.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgos.model.entity.Funcionario
@Dao
interface FuncionarioDao {
    @Insert
    suspend fun inserirFuncionario(funcionario: Funcionario)

    @Query("SELECT * FROM funcionario")
    suspend fun buscarTodos() : List<Funcionario>

    @Delete
    suspend fun deletar(funcionario: Funcionario)

    @Update
    suspend fun atualizar(funcionario: Funcionario)
}