package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.database.dao.ClienteDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class ClienteViewModel(private val clienteDao: ClienteDao): ViewModel() {

    var listaClientes = mutableStateOf(listOf<Cliente>())
        private set

    init {
        initCarregarCliente()
    }

    private fun carregarCliente() {
        viewModelScope.launch {
            listaClientes.value = clienteDao.buscarTodos()
        }
    }

    private fun initCarregarCliente() {
        viewModelScope.launch {
            listaClientes.value = clienteDao.buscarTodos()

            if(clienteDao.buscarTodos().isEmpty()){
                salvarCliente("Clarice Rayssa Brito","457.700.217-56", "47.017.035-9", "57048-098", "Rua Josenilda Rogério da Silva 10", "Antares", "Maceió", "(82) 99921-6123")
                salvarCliente("Samuel Ryan de Paula", "797.777.363-55", "44.432.240-1", "64073-175", "Rua Quarenta 738", "Uruguai", "Teresina", "(86) 98131-9186")
                salvarCliente( "Lívia Carolina Jéssica Galvão", "127.172.608-46", "18.783.147-6", "69033-841", "Beco Santa Tereza 361", "São Jorge", "Manaus", "(92) 99556-5670")
                salvarCliente("Tatiane Simone Corte Real", "181.960.267-22", "22.894.329-2", "38181-078", "Rua Claudionor Afonso de Rezende 314", "Ana Pinto de Almeida", "Araxá", "(34) 99841-0970")
                salvarCliente( "Marli Rayssa dos Santos", "940.800.480-92", "22.865.552-3", "99064-340", "Rua Felipe Moliterno 450", "Vila Mattos", "Passo Fundo", "(54) 98335-2174")
            }
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

    fun salvarCliente(id:Int, nome: String, cpf: String, rg: String, cep: String, endereco: String, bairro: String, cidade: String, telefone: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, cpf, rg, cep, endereco, bairro, cidade, telefone)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo cliente
        val cliente = Cliente(id, dataCadastro = Date(), nome, cpf, rg, cep, endereco, bairro, cidade, telefone)

        // adicionar este cliente na tabela de clientes
        viewModelScope.launch {
            clienteDao.inserirCliente(cliente)
            carregarCliente()
        }

        return "Cliente salvo com sucesso!"

    }

    fun excluirCliente(cliente: Cliente, ordemServicoViewModel:OrdemServicoViewModel) {

        viewModelScope.launch {
            clienteDao.deletar(cliente)
            delay(100)
            carregarCliente()
            ordemServicoViewModel.carregarOrdemServico()
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