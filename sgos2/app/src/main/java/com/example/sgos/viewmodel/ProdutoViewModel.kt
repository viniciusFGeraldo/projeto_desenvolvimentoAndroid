package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.ProdutoDao
import com.example.sgos.model.entity.Produto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class ProdutoViewModel(private val produtoDao: ProdutoDao) : ViewModel() {

    var listaProdutos = mutableStateOf(listOf<Produto>())
        private set

    init {
        initCarregarProdutos()
    }

    fun carregarProdutos(){
        viewModelScope.launch {
            listaProdutos.value = produtoDao.buscarTodos()
        }
    }

    private fun initCarregarProdutos() {
        viewModelScope.launch {
            listaProdutos.value = produtoDao.buscarTodos()

            if(listaProdutos.value.isEmpty()){
                salvarProduto("cartão de visita", "serviço tercerizado", 2, 3)
                salvarProduto("lona front", "lona 440g branco front-light com fundo black", 1, 1)
                salvarProduto("lona backlight", "lona 440g branco back-light com fundo branco", 2, 2)
                salvarProduto("adesivo branco ", "adesivo 10mm para impressão digital", 3, 3)
                salvarProduto("adesivo recorte", "recorte de adesivo colorido para aplicação", 4, 4)
            }
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

    fun excluirProduto(produto: Produto, ordemServicoViewModel:OrdemServicoViewModel) {
        viewModelScope.launch {
            produtoDao.deletar(produto)
            delay(100)
            carregarProdutos()
            ordemServicoViewModel.carregarOrdemServico()
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
