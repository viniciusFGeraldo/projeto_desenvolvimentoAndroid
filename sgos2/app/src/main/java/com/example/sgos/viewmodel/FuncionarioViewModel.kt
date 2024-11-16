package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.FuncionarioDao
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.model.entity.Funcionario
import kotlinx.coroutines.launch
import java.util.Date

class FuncionarioViewModel(private var funcionarioDao: FuncionarioDao): ViewModel() {

    var listaFuncionarios = mutableStateOf(listOf<Funcionario>())
        private set

    init {
        carregarFuncionarios()
    }

    private fun carregarFuncionarios() {
        viewModelScope.launch {
            listaFuncionarios.value = funcionarioDao.buscarTodos()
        }
    }

    fun salvarFuncionario(nome: String, telefone: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, telefone)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo Funcionario
        var funcionario = Funcionario(0, nome, telefone, criadoEm = Date())

        // adicionar este Funcionario na tabela de Funcionarios
        viewModelScope.launch {
            funcionarioDao.inserirFuncionario(funcionario)
            carregarFuncionarios()
        }

        return "Funcionário salvo com sucesso!"

    }

    fun excluirFuncionario(funcionario: Funcionario) {

        viewModelScope.launch {
            funcionarioDao.deletar(funcionario)
            carregarFuncionarios()
        }

    }

    fun atualizarFuncionario(id: Int, nome: String, telefone: String) : String{

        if (Validacao.haCamposEmBranco(nome, telefone)) {
            return ("Ao editar, preencha todos os dados do Funcionario!")
        }

        var funcionario = listaFuncionarios.value.find { it.id == id } ?: return "Erro ao atualizar Funcionario"

        var funcionarioAtualizado = funcionario.copy(nome = nome, telefone = telefone)

        viewModelScope.launch{
            funcionarioDao.atualizar(funcionarioAtualizado)
            carregarFuncionarios()
        }

        return "Funcionarios atualizado!"
    }
}