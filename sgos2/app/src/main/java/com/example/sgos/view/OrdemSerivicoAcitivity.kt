package com.example.sgos.view

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.Funcionario
import com.example.sgos.model.entity.OrdemServico
import com.example.sgos.model.entity.Produto
import com.example.sgos.model.entity.Status
import com.example.sgos.viewmodel.ClienteViewModel
import com.example.sgos.viewmodel.FuncionarioViewModel
import com.example.sgos.viewmodel.OrdemServicoViewModel
import com.example.sgos.viewmodel.ProdutoViewModel


//TELAS DE ORDEM DE SERVIÇO
@Composable
fun ListaOrdemServico(ordemServicoViewModel: OrdemServicoViewModel, clienteViewModel: ClienteViewModel, produtoViewModel: ProdutoViewModel, funcionarioViewModel: FuncionarioViewModel, navController: NavController) {

    // Estados dos campos de entrada
    var largura by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var valorM2 by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var valorUnitario by remember { mutableStateOf("") }
    var valorTotal by remember { mutableStateOf("") } // Novo estado para o valor total
    var observacoes by remember { mutableStateOf("") }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }
    var ordemServicoTemp by remember { mutableStateOf<OrdemServico?>(null) }
    var ordemServicoExcluir by remember { mutableStateOf<OrdemServico?>(null) }

    // Listas de dados vindos dos ViewModels
    val listaOrdemServico by ordemServicoViewModel.listaOrdemServico
    val listaClientes by clienteViewModel.listaClientes
    val listaProdutos by produtoViewModel.listaProdutos
    val listaFuncionarios by funcionarioViewModel.listaFuncionarios

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Estados de seleção para cliente, produto e funcionário
    var clienteSelecionado by remember { mutableStateOf<Cliente?>(null) }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(null) }
    var funcionarioSelecionado by remember { mutableStateOf<Funcionario?>(null) }
    var mostrarSolicitacaoBaixa by remember { mutableStateOf<OrdemServico?>(null) }



    // Calcular automaticamente o valor unitário e o valor total
    LaunchedEffect(largura, altura, valorM2, quantidade) {
        val larguraFloat = largura.toFloatOrNull() ?: 0f
        val alturaFloat = altura.toFloatOrNull() ?: 0f
        val valorM2Float = valorM2.toFloatOrNull() ?: 0f
        val quantidadeInt = quantidade.toIntOrNull() ?: 0

        // Calculando o valor unitário
        val calculatedValorUnitario = (larguraFloat * alturaFloat) * valorM2Float
        valorUnitario = calculatedValorUnitario.toString()

        // Calculando o valor total
        val calculatedValorTotal = calculatedValorUnitario * quantidadeInt
        valorTotal = calculatedValorTotal.toString()
    }

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirOrdemServico(onConfirm = {
            ordemServicoExcluir?.let { ordemServicoViewModel.excluirOrdemServico(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Lista de Ordens de Serviço", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(15.dp))

        // ComboBox para selecionar o cliente
        var expandedCliente by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = { expandedCliente = true }) {
                Text(clienteSelecionado?.nome ?: "Selecione um Cliente")
            }
            DropdownMenu(
                expanded = expandedCliente,
                onDismissRequest = { expandedCliente = false }
            ) {
                listaClientes.forEach { cliente ->
                    DropdownMenuItem(
                        text = { Text(text = cliente.nome) },
                        onClick = {
                            clienteSelecionado = cliente
                            expandedCliente = false
                        }
                    )
                }
            }
        }

        // ComboBox para selecionar o funcionário
        var expandedFuncionario by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = { expandedFuncionario = true }) {
                Text(funcionarioSelecionado?.nome ?: "Selecione um Funcionário")
            }
            DropdownMenu(
                expanded = expandedFuncionario,
                onDismissRequest = { expandedFuncionario = false }
            ) {
                listaFuncionarios.forEach { funcionario ->
                    DropdownMenuItem(
                        text = { Text(text = funcionario.nome) },
                        onClick = {
                            funcionarioSelecionado = funcionario
                            expandedFuncionario = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = observacoes,
            onValueChange = { observacoes = it },
            label = { Text("Observações") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))
        // ComboBox para selecionar o produto
        var expandedProduto by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = { expandedProduto = true }) {
                Text(produtoSelecionado?.nome ?: "Selecione um Produto")
            }
            DropdownMenu(
                expanded = expandedProduto,
                onDismissRequest = { expandedProduto = false }
            ) {
                listaProdutos.forEach { produto ->
                    DropdownMenuItem(
                        text = { Text(text = produto.nome) },
                        onClick = {
                            produtoSelecionado = produto
                            expandedProduto = false
                        }
                    )
                }
            }
        }
        TextField(
            value = largura,
            onValueChange = { largura = it },
            label = { Text("Largura") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = valorM2,
            onValueChange = { valorM2 = it },
            label = { Text("Valor por M2") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Agora, valorUnitario é calculado automaticamente e só é exibido, sem possibilidade de edição
        TextField(
            value = valorUnitario,
            onValueChange = { }, // Não permite edição manual
            label = { Text("Valor Unitário") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Desabilitado
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Novo campo para o valor total
        TextField(
            value = valorTotal,
            onValueChange = { }, // Não permite edição manual
            label = { Text("Valor Total") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = false // Desabilitado
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Botão de Salvar ou Atualizar
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (clienteSelecionado == null || produtoSelecionado == null || funcionarioSelecionado == null) {
                    Toast.makeText(context, "Selecione um Cliente, Produto e Funcionário.", Toast.LENGTH_LONG).show()
                } else {
                    val retorno: String = if (modoEditar) {
                        ordemServicoTemp?.let {
                            // Agora passando valorTotal no lugar de valorUnitario * quantidade
                            ordemServicoViewModel.atualizarOrdemServico(
                                it.id,
                                largura.toFloat(),
                                altura.toFloat(),
                                valorM2.toFloat(),
                                quantidade.toInt(),
                                valorUnitario.toFloat(),
                                valorTotal.toFloat(), // Usando valorTotal calculado
                                0f, // Definindo valorDesconto como 0 por padrão, pode ser alterado
                                0f, // valorAPagar é igual ao valorTotal por enquanto
                                observacoes,
                                Status.EM_PRODUCAO, // Status fixo, pode ser alterado conforme a lógica
                                clienteSelecionado!!.id, // Passando o clienteId
                                funcionarioSelecionado!!.id, // Passando o funcionarioId
                                produtoSelecionado!!.id // Passando o produtoId
                            ).also {
                                modoEditar = false
                                textoBotao = "Salvar"
                            }
                        } ?: "Erro ao editar ordem de serviço"
                    } else {
                        ordemServicoViewModel.salvarOrdemServico(
                            largura.toFloat(),
                            altura.toFloat(),
                            valorM2.toFloat(),
                            quantidade.toInt(),
                            valorUnitario.toFloat(),
                            valorTotal.toFloat(), // Usando valorTotal calculado
                            0f,
                            0f,
                            observacoes,
                            Status.EM_PRODUCAO,
                            clienteSelecionado!!.id,
                            funcionarioSelecionado!!.id,
                            produtoSelecionado!!.id
                        )
                    }

                    Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                    // Limpar os campos
                    largura = ""
                    altura = ""
                    valorM2 = ""
                    quantidade = ""
                    valorUnitario = ""
                    valorTotal = ""
                    observacoes = ""
                    clienteSelecionado = null
                    produtoSelecionado = null
                    funcionarioSelecionado = null
                    focusManager.clearFocus()
                }
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))


        LazyColumn {
            items(listaOrdemServico) { ordemServico ->

                val cliente = listaClientes.find { it.id == ordemServico.clienteId }
                val funcionario = listaFuncionarios.find { it.id == ordemServico.funcionarioId }
                val produto = listaProdutos.find { it.id == ordemServico.produtoId }

                Text(
                    text = "Ordem de Serviço: ${ordemServico.id}",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(5.dp))

                // Exibe o nome do cliente
                cliente?.let {
                    Text(
                        text = "Cliente: ${it.nome}",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                funcionario?.let {
                    Text(
                        text = "Funcionário: ${it.nome}",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                produto?.let {
                    Text(
                        text = "Produto: ${it.nome}",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Status: ${ordemServico.status}")

                Row {
                    Button(onClick = {
                        ordemServicoExcluir = ordemServico
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        ordemServicoTemp = ordemServico
                        largura = ordemServico.largura.toString()
                        altura = ordemServico.altura.toString()
                        valorM2 = ordemServico.valorM2.toString()
                        quantidade = ordemServico.quantidade.toString()
                        valorUnitario = ordemServico.valorUnitario.toString()
                        valorTotal = ordemServico.valorTotal.toString()
                        observacoes = ordemServico.observacoes
                        textoBotao = "Atualizar"
                    }) {
                        Text(text = "Editar")
                    }

                    // Verificar se o status é 'SOLICITADO_BAIXA' e abrir o Composable de Solicitação de Baixa
                    if (ordemServico.status == Status.SOLICITADO_BAIXA) {
                        Button(onClick = {
                            mostrarSolicitacaoBaixa = ordemServico
                        }) {
                            Text("Solicitar Baixa")
                        }
                    } else {
                        Button(onClick = {
                            ordemServicoViewModel.atualizarStatus(ordemServico)
                        }) {
                            Text("Atualizar Status")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        if (mostrarSolicitacaoBaixa != null) {
            SolicitarBaixa(
                ordemServico = mostrarSolicitacaoBaixa!!,
                onSolicitar = { ordemServico ->
                    // Realizar a solicitação de baixa (pode envolver um request para o servidor ou uma alteração de status)
                    ordemServicoViewModel.atualizarStatus(ordemServico.copy(status = Status.BAIXADA))
                    mostrarSolicitacaoBaixa = null // Fechar a tela de solicitação
                },
                onDismiss = {
                    mostrarSolicitacaoBaixa = null // Fechar a tela de solicitação sem fazer nada
                }
            )
        }

    }
}

@Composable
fun ExcluirOrdemServico(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir está ordem de serviço?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Sim, excluir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Não, cancelar")
            }
        }
    )
}

@Composable
fun SolicitarBaixa(ordemServico: OrdemServico, onSolicitar: (OrdemServico) -> Unit, onDismiss: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Solicitação de Baixa", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Ordem de Serviço: ${ordemServico.id}", fontSize = 18.sp)
        Text(text = "Cliente: ${ordemServico.clienteId}", fontSize = 16.sp) // Você pode buscar o cliente pelo ID
        Text(text = "Produto: ${ordemServico.produtoId}", fontSize = 16.sp) // Você pode buscar o produto pelo ID
        Text(text = "Status Atual: ${ordemServico.status}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSolicitar(ordemServico)  // Aqui você pode enviar a solicitação
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Solicitação de Baixa")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onDismiss() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}