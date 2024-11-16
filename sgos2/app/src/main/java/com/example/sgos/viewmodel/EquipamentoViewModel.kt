package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.ClienteDao
import com.example.sgos.model.database.dao.EquipamentoDao
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.Equipamento
import kotlinx.coroutines.launch
import java.util.Date

class EquipamentoViewModel(private val equipamentoDao: EquipamentoDao): ViewModel() {

    var listaEquipamentos = mutableStateOf(listOf<Equipamento>())
        private set

    init {
        carregarEquipamentos()
    }

    private fun carregarEquipamentos() {
        viewModelScope.launch {
            listaEquipamentos.value = equipamentoDao.buscarTodos()
        }
    }

    fun salvarEquipamento(nome: String, descricao: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo equipamento
        var equipamento = Equipamento(0, criadoEm = Date(), nome, descricao)

        // adicionar este equipamento na tabela de equipamentos
        viewModelScope.launch {
            equipamentoDao.inserirEquipamento(equipamento)
            carregarEquipamentos()
        }

        return "Equipamento salvo com sucesso!"

    }

    fun excluirEquipamento(equipamento: Equipamento) {

        viewModelScope.launch {
            equipamentoDao.deletar(equipamento)
            carregarEquipamentos()
        }

    }

    fun atualizarEquipamento(id: Int, nome: String, descricao: String) : String{

        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return ("Ao editar, preencha todos os dados do equipamento!")
        }

        var equipamento = listaEquipamentos.value.find { it.id == id } ?: return "Erro ao atualizar equipamento"

        var equipamentoAtualizado = equipamento.copy(nome = nome, descricao = descricao)

        viewModelScope.launch{
            equipamentoDao.atualizar(equipamentoAtualizado)
            carregarEquipamentos()
        }

        return "Equipamento atualizado!"
    }
}