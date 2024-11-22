package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.database.dao.ClienteDao
import kotlinx.coroutines.launch
import java.util.Date

class ClienteViewModel(private val clienteDao: ClienteDao): ViewModel() {

    var listaClientes = mutableStateOf(listOf<Cliente>())
        private set

    init {
        carregarCliente()
    }

    private fun carregarCliente() {
        viewModelScope.launch {
            listaClientes.value = clienteDao.buscarTodos()
        }
    }

    fun salvarCliente(nome: String, cpf: String, rg: String, cep: String, endereco: String, bairro: String, cidade: String, telefone: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, cpf, rg, cep, endereco, bairro, cidade, telefone)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo cliente
        val cliente = Cliente(0, dataCadastro = Date(), nome, cpf, rg, cep, endereco, bairro, cidade, telefone)

        // adicionar este cliente na tabela de clientes
        viewModelScope.launch {
            clienteDao.inserirCliente(cliente)
            carregarCliente()
        }

        return "Cliente salvo com sucesso!"

    }

    fun excluirCliente(cliente: Cliente) {

        viewModelScope.launch {
            clienteDao.deletar(cliente)
            carregarCliente()
        }

    }

    fun atualizarCliente(id: Int, nome: String, cpf: String, rg: String, cep: String, endereco: String, bairro: String, cidade: String, telefone: String) : String{

        if (Validacao.haCamposEmBranco(nome, cpf, rg, cep, endereco, bairro, cidade, telefone)) {
            return ("Ao editar, preencha todos os dados do cliente!")
        }

        val cliente = listaClientes.value.find { it.id == id } ?: return "Erro ao atualizar cliente"

        val clienteAtualizado = cliente.copy(nome = nome, cpf = cpf, rg = rg, cep = cep, endereco = endereco, bairro = bairro, cidade = cidade, telefone = telefone)

        viewModelScope.launch{
            clienteDao.atualizar(clienteAtualizado)
            carregarCliente()
        }

        return "Cliente atualizado!"
    }

}