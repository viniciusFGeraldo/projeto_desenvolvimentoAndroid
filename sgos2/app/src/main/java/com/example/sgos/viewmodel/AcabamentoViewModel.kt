package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.AcabamentoDao
import com.example.sgos.model.entity.Acabamento
import kotlinx.coroutines.launch
import java.util.Date

class AcabamentoViewModel(private val acabamentoDao: AcabamentoDao): ViewModel() {

    var listaAcabamentos = mutableStateOf(listOf<Acabamento>())
        private set

    init {
        carregarAcabamento()
    }

    private fun carregarAcabamento() {
        viewModelScope.launch {
            listaAcabamentos.value = acabamentoDao.buscarTodos()
        }
    }

    fun salvarAcabamento(nome: String, descricao: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo acabamento
        var acabamento = Acabamento(0, criadoEm = Date(),nome, descricao)

        // adicionar este acabamento na tabela de acabamentos
        viewModelScope.launch {
            acabamentoDao.inserirAcabamento(acabamento)
            carregarAcabamento()
        }

        return "Acabamento salvo com sucesso!"

    }

    fun excluirAcabamento(acabamento: Acabamento) {

        viewModelScope.launch {
            acabamentoDao.deletar(acabamento)
            carregarAcabamento()
        }

    }

    fun atualizarAcabamento(id: Int, nome: String, descricao: String) : String{

        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return ("Ao editar, preencha todos os dados do acabamento!")
        }

        var acabamento = listaAcabamentos.value.find { it.id == id } ?: return "Erro ao atualizar acabamento"

        var acabamentoAtualizado = acabamento.copy(nome = nome, descricao = descricao)

        viewModelScope.launch{
            acabamentoDao.atualizar(acabamentoAtualizado)
            carregarAcabamento()
        }

        return "Acabamento atualizado!"

    }
}