package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.FuncionarioDao
import com.example.sgos.model.entity.Funcionario
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class FuncionarioViewModel(private var funcionarioDao: FuncionarioDao): ViewModel() {

    var listaFuncionarios = mutableStateOf(listOf<Funcionario>())
        private set

    init {
        initCarregarFuncionarios()
    }

    private fun carregarFuncionarios() {
        viewModelScope.launch {
            listaFuncionarios.value = funcionarioDao.buscarTodos()
        }
    }

    private fun initCarregarFuncionarios() {
        viewModelScope.launch {
            listaFuncionarios.value = funcionarioDao.buscarTodos()

            if(listaFuncionarios.value.isEmpty()){
                salvarFuncionario("Danilo Juan Cláudio das Neves", "(79) 98932-4794")
                salvarFuncionario("Juliana Julia Alícia da Rocha", "(27) 98783-4120")
                salvarFuncionario("Cristiane Regina da Luz", "(34) 99490-4977")
                salvarFuncionario("Jéssica Brenda Bernardes", "(11) 99756-8815")
                salvarFuncionario("Maria Benedita Mariane Almada", "(48) 98901-4168")
            }
        }
    }

    fun salvarFuncionario(nome: String, telefone: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, telefone)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo Funcionario
        val funcionario = Funcionario(0, nome, telefone, criadoEm = Date())

        // adicionar este Funcionario na tabela de Funcionarios
        viewModelScope.launch {
            funcionarioDao.inserirFuncionario(funcionario)
            carregarFuncionarios()
        }

        return "Funcionário salvo com sucesso!"

    }

    fun excluirFuncionario(funcionario: Funcionario, ordemServicoViewModel:OrdemServicoViewModel) {

        viewModelScope.launch {
            funcionarioDao.deletar(funcionario)
            delay(100)

            carregarFuncionarios()
            ordemServicoViewModel.carregarOrdemServico()
        }

    }

    fun atualizarFuncionario(id: Int, nome: String, telefone: String) : String{

        if (Validacao.haCamposEmBranco(nome, telefone)) {
            return ("Ao editar, preencha todos os dados do Funcionario!")
        }

        val funcionario = listaFuncionarios.value.find { it.id == id } ?: return "Erro ao atualizar Funcionario"

        val funcionarioAtualizado = funcionario.copy(nome = nome, telefone = telefone)

        viewModelScope.launch{
            funcionarioDao.atualizar(funcionarioAtualizado)
            carregarFuncionarios()
        }

        return "Funcionarios atualizado!"
    }
}