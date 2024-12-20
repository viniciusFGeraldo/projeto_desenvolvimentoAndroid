package com.example.sgos.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun carregarOrdemServico() {
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

    fun atualizarOrdemServico(id: Int, ordemServicoNova: OrdemServico): String {

        val ordemServico = listaOrdemServico.value.find { it.id == id } ?: return "Erro ao atualizar ordem de serviço"

        val ordemServicoAtualizada = ordemServico.copy(
            largura = ordemServicoNova.largura,
            altura = ordemServicoNova.altura,
            valorM2 = ordemServicoNova.valorM2,
            quantidade = ordemServicoNova.quantidade,
            valorUnitario = ordemServicoNova.valorUnitario,
            valorTotal = ordemServicoNova.valorTotal,
            valorDesconto = ordemServicoNova.valorDesconto,
            valorAPagar = ordemServicoNova.valorAPagar,
            observacoes = ordemServicoNova.observacoes,
            status = ordemServicoNova.status,
            clienteId = ordemServicoNova.clienteId,
            funcionarioId = ordemServicoNova.funcionarioId,
            produtoId = ordemServicoNova.produtoId
        )

        viewModelScope.launch {
            ordemServicoDao.atualizar(ordemServicoAtualizada)
            carregarOrdemServico()
        }

        return "Ordem de serviço atualizada com sucesso!"
    }

    fun solicitarBaixa(id: Int, desconto: Float): String {

        val ordemServico = listaOrdemServico.value.find { it.id == id } ?: return "Erro ao solicitar baixa de ordem de serviço"


        val ordemServicoAtualizada = ordemServico.copy(
            valorDesconto = desconto,
            valorAPagar = ordemServico.valorTotal - desconto
        )

        viewModelScope.launch {
            ordemServicoDao.atualizar(ordemServicoAtualizada)
            carregarOrdemServico()
        }

        atualizarStatus(ordemServicoAtualizada);

        return "Solicitação de Baixa efetuada com sucesso!"
    }

    fun atualizarStatus(ordemServico: OrdemServico) {
        viewModelScope.launch {
            val ordemServicoAtualizada = ordemServico.copy(status = ordemServico.status.nextStatus())
            ordemServicoDao.atualizar(ordemServicoAtualizada)
            carregarOrdemServico()
        }
    }

}
