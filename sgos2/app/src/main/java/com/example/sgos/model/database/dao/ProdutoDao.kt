package com.example.sgos.model.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sgos.model.entity.Produto

interface ProdutoDao {

    @Insert
    suspend fun inserirProduto(produto: Produto)

    @Query("SELECT * FROM produto")
    suspend fun buscarTodos() : List<Produto>

    @Delete
    suspend fun deletar(produto: Produto)

    @Update
    suspend fun atualizar(produto: Produto)
}