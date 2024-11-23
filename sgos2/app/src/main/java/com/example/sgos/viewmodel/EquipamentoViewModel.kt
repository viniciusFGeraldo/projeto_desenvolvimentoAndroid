package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.EquipamentoDao
import com.example.sgos.model.entity.Equipamento
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class EquipamentoViewModel(private val equipamentoDao: EquipamentoDao): ViewModel() {

    var listaEquipamentos = mutableStateOf(listOf<Equipamento>())
        private set

    init {
        initCarregarEquipamentos()
    }

    private fun carregarEquipamentos() {
        viewModelScope.launch {
            listaEquipamentos.value = equipamentoDao.buscarTodos()
        }
    }

    private fun initCarregarEquipamentos() {
        viewModelScope.launch {
            listaEquipamentos.value = equipamentoDao.buscarTodos()
            if(listaEquipamentos.value.isEmpty()){
                salvarEquipamento("My Jet", "máquina de impressão digital para grandes formatos com tamanho de boca 3.20m e com tinta a base de solvente")
                salvarEquipamento("Roland", "máquina para pequenos formatos com tamanho de boca de 1.60 e com tinta a base de solvente")
                salvarEquipamento("Plotter", "máquina para corte de vinil de até 1.27m")
                salvarEquipamento("Gráfica", "parte tercerizada como cartão de visita e flayer")
            }
        }
    }

    fun salvarEquipamento(nome: String, descricao: String) : String {

        // se algum dos dados estiver em branco:
        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return "Preencha todos os campos!"
        }

        // cria objeto do tipo equipamento
        val equipamento = Equipamento(0, criadoEm = Date(), nome, descricao)

        // adicionar este equipamento na tabela de equipamentos
        viewModelScope.launch {
            equipamentoDao.inserirEquipamento(equipamento)
            carregarEquipamentos()
        }

        return "Equipamento salvo com sucesso!"

    }

    fun excluirEquipamento(equipamento: Equipamento, ordemServicoViewModel:OrdemServicoViewModel, produtoViewModel: ProdutoViewModel) {

        viewModelScope.launch {
            equipamentoDao.deletar(equipamento)
            delay(100)
            carregarEquipamentos()
            ordemServicoViewModel.carregarOrdemServico()
            produtoViewModel.carregarProdutos()
        }

    }

    fun atualizarEquipamento(id: Int, nome: String, descricao: String) : String{

        if (Validacao.haCamposEmBranco(nome, descricao)) {
            return ("Ao editar, preencha todos os dados do equipamento!")
        }

        val equipamento = listaEquipamentos.value.find { it.id == id } ?: return "Erro ao atualizar equipamento"

        val equipamentoAtualizado = equipamento.copy(nome = nome, descricao = descricao)

        viewModelScope.launch{
            equipamentoDao.atualizar(equipamentoAtualizado)
            carregarEquipamentos()
        }

        return "Equipamento atualizado!"
    }
}