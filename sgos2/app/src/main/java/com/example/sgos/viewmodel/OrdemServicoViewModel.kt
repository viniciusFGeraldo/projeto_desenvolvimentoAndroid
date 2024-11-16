package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sgos.model.Validacao
import com.example.sgos.model.database.dao.OrdemServicoDao
import com.example.sgos.model.entity.OrdemServico
import com.example.sgos.model.entity.Status
import kotlinx.coroutines.launch
import java.util.Date

class OrdemServicoViewModel(private val ordemServicoDao: OrdemServicoDao) : ViewModel() {

    var listaOrdemServico = mutableStateOf(listOf<OrdemServico>())
        private set

    init {
        carregarOrdemServico()
    }

    private fun carregarOrdemServico() {
        viewModelScope.launch {
            listaOrdemServico.value = ordemServicoDao.buscarTodos()
        }
    }

    fun salvarOrdemServico(largura: Float, altura:Float, valorM2: Float, quantidade: Int, valorUnitario: Float,
        valorTotal: Float, valorDesconto: Float, valorAPagar: Float, observacoes: String,
        status: Status, clienteId: Int, funcionarioId: Int, produtoId: Int
    ): String {


        val ordemServico = OrdemServico(
            id = 0,
            largura = largura,
            altura = altura,
            valorM2 = valorM2,
            quantidade = quantidade,
            valorUnitario = valorUnitario,
            valorTotal = valorTotal,
            valorDesconto = valorDesconto,
            valorAPagar = valorAPagar,
            observacoes = observacoes,
            dataHorarioAbertura = Date(),
            status = status,
            clienteId = clienteId,
            funcionarioId = funcionarioId,
            produtoId = produtoId
        )

        viewModelScope.launch {
            ordemServicoDao.inserirOrdemServico(ordemServico)
            carregarOrdemServico()
        }

        return "Ordem de serviço salva com sucesso!"
    }

    fun excluirOrdemServico(ordemServico: OrdemServico) {
        viewModelScope.launch {
            ordemServicoDao.deletar(ordemServico)
            carregarOrdemServico()
        }
    }

    fun atualizarOrdemServico(
        id: Int, largura: Float, altura:Float, valorM2: Float, quantidade: Int, valorUnitario: Float,
        valorTotal: Float, valorDesconto: Float, valorAPagar: Float, observacoes: String,
        status: Status, clienteId: Int, funcionarioId: Int, produtoId: Int
    ): String {

        val ordemServico = listaOrdemServico.value.find { it.id == id } ?: return "Erro ao atualizar ordem de serviço"

        val ordemServicoAtualizada = ordemServico.copy(
            largura = largura,
            altura = altura,
            valorM2 = valorM2,
            quantidade = quantidade,
            valorUnitario = valorUnitario,
            valorTotal = valorTotal,
            valorDesconto = valorDesconto,
            valorAPagar = valorAPagar,
            observacoes = observacoes,
            status = status,
            clienteId = clienteId,
            funcionarioId = funcionarioId,
            produtoId = produtoId
        )

        viewModelScope.launch {
            ordemServicoDao.atualizar(ordemServicoAtualizada)
            carregarOrdemServico()
        }

        return "Ordem de serviço atualizada com sucesso!"
    }
}
