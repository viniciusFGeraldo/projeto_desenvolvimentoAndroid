package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.AcabamentoDao
import com.example.sgos.model.entity.Acabamento
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class AcabamentoViewModel(private val acabamentoDao: AcabamentoDao): ViewModel() {

    var listaAcabamentos = mutableStateOf(listOf<Acabamento>())
        private set

    init {
        initCarregarAcabamento()
    }

    private fun carregarAcabamento() {
        viewModelScope.launch {
            listaAcabamentos.value = acabamentoDao.buscarTodos()
        }
    }

    private fun initCarregarAcabamento() {
        viewModelScope.launch {
            listaAcabamentos.value = acabamentoDao.buscarTodos()

            if(listaAcabamentos.value.isEmpty()){
                salvarAcabamento("Soldar lona e prender ilhós", "após a impressão,refila a lona e passa pela máquina de solda e depois passa pela ilhoseira prendendo o ilhós")
                salvarAcabamento("Acabamento para banner", "após a impressão,refila a lona,solda para formar as bolsas do banner e coloca as madeiras com barbante")
                salvarAcabamento("Sobra", "sobra é um espaço branco que fica em volta da lona para que ela possa ser esticada")
                salvarAcabamento("Acabamento em quadro", "após a impressão a lona é esticada em um quadro de metalon para instalação")
                salvarAcabamento("Acabamento refile", "Após a impressão é feito o refile manual podendo ser adesivo ou lona")
                salvarAcabamento("Acabamento depile e mascarar", "após a impressão do corte contorno ou recorte do adesivo é feito o depile do excesso de adesivo e depois é mascarado se necessario")
                salvarAcabamento("Instalação de adesivo", "após a produção é necessario ir até o cliente instalar/aplicar")
                salvarAcabamento("Instalação de lona", "após a produção é necessario ir ao cliente instalar a lona")
                salvarAcabamento("Acabamento gráfico", "acabamento de cartão de visita e flayer")
            }
        }
    }

    fun salvarAcabamento(nome: String, descricao: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo acabamento
        val acabamento = Acabamento(0, criadoEm = Date(),nome, descricao)

        // adicionar este acabamento na tabela de acabamentos
        viewModelScope.launch {
            acabamentoDao.inserirAcabamento(acabamento)
            carregarAcabamento()
        }

        return "Acabamento salvo com sucesso!"

    }

    fun excluirAcabamento(acabamento: Acabamento, ordemServicoViewModel:OrdemServicoViewModel, produtoViewModel: ProdutoViewModel) {

        viewModelScope.launch {
            acabamentoDao.deletar(acabamento)
            delay(100)
            carregarAcabamento()
            ordemServicoViewModel.carregarOrdemServico()
            produtoViewModel.carregarProdutos()
        }

    }

    fun atualizarAcabamento(id: Int, nome: String, descricao: String) : String{

        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return ("Ao editar, preencha todos os dados do acabamento!")
        }

        val acabamento = listaAcabamentos.value.find { it.id == id } ?: return "Erro ao atualizar acabamento"

        val acabamentoAtualizado = acabamento.copy(nome = nome, descricao = descricao)

        viewModelScope.launch{
            acabamentoDao.atualizar(acabamentoAtualizado)
            carregarAcabamento()
        }

        return "Acabamento atualizado!"

    }
}