package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.ProdutoDao
import com.example.sgos.model.entity.Produto
import kotlinx.coroutines.launch
import java.util.Date

class ProdutoViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    var listaProdutos = mutableStateOf(listOf<Produto>())
        private set

    init {
        carregarProdutos()
    }

    private fun carregarProdutos() {
        viewModelScope.launch {
            listaProdutos.value = produtoDao.buscarTodos()
        }
    }

    fun salvarProduto(
        nome: String, descricao: String, acabamentoId: Int, equipamentoId: Int
    ): String {

        // Valida se algum campo está vazio
        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return "Preencha todos os campos obrigatórios!"
        }

        // Cria o objeto Produto
        val produto = Produto(
            id = 0,
            nome = nome,
            descricao = descricao,
            criadoEm = Date(),
            acabamentoId = acabamentoId,
            equipamentoId = equipamentoId
        )

        // adiciona este produto na tabela de produtos
        viewModelScope.launch {
            produtoDao.inserirProduto(produto)
            carregarProdutos()
        }

        return "Produto salvo com sucesso!"
    }

    fun excluirProduto(produto: Produto) {
        viewModelScope.launch {
            produtoDao.deletar(produto)
            carregarProdutos()
        }
    }

    fun atualizarProduto(id: Int, nome: String, descricao: String, acabamentoId: Int, equipamentoId: Int): String {

        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return "Ao editar, preencha todos os campos obrigatórios!"
        }

        val produto = listaProdutos.value.find { it.id == id } ?: return "Erro ao atualizar produto"

        val produtoAtualizado = produto.copy(
            nome = nome,
            descricao = descricao,
            acabamentoId = acabamentoId,
            equipamentoId = equipamentoId
        )


        viewModelScope.launch {
            produtoDao.atualizar(produtoAtualizado)
            carregarProdutos()
        }

        return "Produto atualizado com sucesso!"
    }
}
