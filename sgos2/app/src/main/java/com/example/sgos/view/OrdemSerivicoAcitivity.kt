package com.example.sgos.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
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
import com.google.gson.Gson


@Composable
fun ListaOrdemServico(
    modoSolicitacaoBaixa: Boolean,
    ordemServicoViewModel: OrdemServicoViewModel,
    clienteViewModel: ClienteViewModel,
    produtoViewModel: ProdutoViewModel,
    funcionarioViewModel: FuncionarioViewModel,
    navController: NavController
) {
    val listaOrdemServico by ordemServicoViewModel.listaOrdemServico
    val listaClientes by clienteViewModel.listaClientes
    val listaProdutos by produtoViewModel.listaProdutos
    val listaFuncionarios by funcionarioViewModel.listaFuncionarios

    var ordemServicoExcluir by remember { mutableStateOf<OrdemServico?>(null) }
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirOrdemServico(onConfirm = {
            ordemServicoExcluir?.let { ordemServicoViewModel.excluirOrdemServico(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botões superiores
        if (!modoSolicitacaoBaixa) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.navigate("listaSolicitacoesBaixa") }) {
                    Text("Solicitações de Baixas")
                }
                Button(onClick = { navController.navigate("cadastroOrdemServico") }) {
                    Text("Cadastrar")
                }
            }
        } else {
            Button(onClick = { navController.popBackStack() }) {
                Text("Voltar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de ordens de serviço
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listaOrdemServico) { ordemServico ->
                if ((!modoSolicitacaoBaixa && ordemServico.status != Status.SOLICITADO_BAIXA) ||
                    (modoSolicitacaoBaixa && ordemServico.status == Status.SOLICITADO_BAIXA)
                ) {
                    val cliente = listaClientes.find { it.id == ordemServico.clienteId }
                    val funcionario = listaFuncionarios.find { it.id == ordemServico.funcionarioId }
                    val produto = listaProdutos.find { it.id == ordemServico.produtoId }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        onClick = {
                            navController.navigate("mostrarInformacaoOS/${ordemServico.id}")
                        }

                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Ordem de Serviço: ${ordemServico.id}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            cliente?.let {
                                Text(text = "Cliente: ${it.nome}", style = MaterialTheme.typography.bodySmall)
                            }
                            funcionario?.let {
                                Text(text = "Funcionário: ${it.nome}", style = MaterialTheme.typography.bodySmall)
                            }
                            produto?.let {
                                Text(text = "Produto: ${it.nome}", style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                text = "Status: ${ordemServico.status}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun formOrdemServico(
    modoEditar: Boolean,
    ordemServico: OrdemServico?,
    ordemServicoViewModel: OrdemServicoViewModel,
    clienteViewModel: ClienteViewModel,
    produtoViewModel: ProdutoViewModel,
    funcionarioViewModel: FuncionarioViewModel,
    navController: NavController
) {
    var largura by remember { mutableStateOf(if(modoEditar) "${ordemServico?.largura}" else "") }
    var altura by remember { mutableStateOf(if(modoEditar) "${ordemServico?.altura}" else "") }
    var valorM2 by remember { mutableStateOf(if(modoEditar) "${ordemServico?.valorM2}" else "") }
    var quantidade by remember { mutableStateOf(if(modoEditar) "${ordemServico?.quantidade}" else "") }
    var valorUnitario by remember { mutableStateOf(if(modoEditar) "${ordemServico?.valorUnitario}" else "") }
    var valorTotal by remember { mutableStateOf(if(modoEditar) "${ordemServico?.valorTotal}" else "") }
    var observacoes by remember { mutableStateOf(if(modoEditar) "${ordemServico?.observacoes}" else "") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val listaClientes by clienteViewModel.listaClientes
    val listaProdutos by produtoViewModel.listaProdutos
    val listaFuncionarios by funcionarioViewModel.listaFuncionarios

    var clienteSelecionado by remember { mutableStateOf<Cliente?>(if(modoEditar) listaClientes.find { it.id == ordemServico?.clienteId ?: null } else null ) }
    var produtoSelecionado by remember { mutableStateOf<Produto?>(if(modoEditar) listaProdutos.find { it.id == ordemServico?.produtoId ?: null } else null ) }
    var funcionarioSelecionado by remember { mutableStateOf<Funcionario?>(if(modoEditar) listaFuncionarios.find { it.id == ordemServico?.funcionarioId ?: null } else null ) }

    // Atualização automática dos cálculos
    LaunchedEffect(largura, altura, valorM2, quantidade) {
        val larguraFloat = largura.toFloatOrNull() ?: 0f
        val alturaFloat = altura.toFloatOrNull() ?: 0f
        val valorM2Float = valorM2.toFloatOrNull() ?: 0f
        val quantidadeInt = quantidade.toIntOrNull() ?: 0

        val calculatedValorUnitario = (larguraFloat * alturaFloat) * valorM2Float
        valorUnitario = calculatedValorUnitario.toString()

        val calculatedValorTotal = calculatedValorUnitario * quantidadeInt
        valorTotal = calculatedValorTotal.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 30.dp, 10.dp, 30.dp)
            .size(100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = if (modoEditar) "Editar Ordem de Serviço" else "Cadastrar Ordem de Serviço",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Cliente
        DropdownMenuField(
            label = "Cliente",
            selectedText = clienteSelecionado?.nome.orEmpty(),
            options = listaClientes,
            onOptionSelected = { clienteSelecionado = it }
        )

        // Funcionário
        DropdownMenuField(
            label = "Funcionário",
            selectedText = funcionarioSelecionado?.nome.orEmpty(),
            options = listaFuncionarios,
            onOptionSelected = { funcionarioSelecionado = it }
        )

        // Produto
        DropdownMenuField(
            label = "Produto",
            selectedText = produtoSelecionado?.nome.orEmpty(),
            options = listaProdutos,
            onOptionSelected = { produtoSelecionado = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos numéricos
        OutlinedTextFieldWithSpacing(
            value = largura,
            onValueChange = { largura = it },
            label = "Largura",
            keyboardType = KeyboardType.Number
        )
        OutlinedTextFieldWithSpacing(
            value = altura,
            onValueChange = { altura = it },
            label = "Altura",
            keyboardType = KeyboardType.Number
        )
        OutlinedTextFieldWithSpacing(
            value = valorM2,
            onValueChange = { valorM2 = it },
            label = "Valor por M2",
            keyboardType = KeyboardType.Number
        )
        OutlinedTextFieldWithSpacing(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = "Quantidade",
            keyboardType = KeyboardType.Number
        )

        // Campos somente leitura
        ReadOnlyTextFieldWithSpacing(value = valorUnitario, label = "Valor Unitário")
        ReadOnlyTextFieldWithSpacing(value = valorTotal, label = "Valor Total")

        Spacer(modifier = Modifier.height(16.dp))

        // Observações
        OutlinedTextFieldWithSpacing(
            value = observacoes,
            onValueChange = { observacoes = it },
            label = "Observações",
            singleLine = false,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão principal
        Button(
            onClick = {
                if (clienteSelecionado == null || produtoSelecionado == null || funcionarioSelecionado == null) {
                    Toast.makeText(
                        context,
                        "Selecione um Cliente, Produto e Funcionário.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val retorno: String = if(modoEditar) {
                        ordemServico?.let {
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
                            )
                        } ?: "Erro ao editar ordem de serviço"
                    }else {
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
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (modoEditar) "Salvar Alterações" else "Cadastrar Ordem de Serviço")
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// Composable para Dropdown
@Composable
fun <T> DropdownMenuField(
    label: String,
    selectedText: String,
    options: List<T>,
    onOptionSelected: (T) -> Unit
) where T : Any {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedText.ifEmpty { "Selecione" })
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.toString()) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Campos de texto estilizados
@Composable
fun OutlinedTextFieldWithSpacing(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        singleLine = singleLine,
        maxLines = maxLines,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

// Campo somente leitura
@Composable
fun ReadOnlyTextFieldWithSpacing(value: String, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        enabled = false
    )
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
fun MostrarInformacaoOS(
    ordemServico: OrdemServico,
    navController: NavController,
    ordemServicoViewModel: OrdemServicoViewModel,
    produtoViewModel: ProdutoViewModel,
    clienteViewModel: ClienteViewModel,
    funcionarioViewModel: FuncionarioViewModel,
    context: Context
) {
    val clienteNome = clienteViewModel.listaClientes.value.find { it.id == ordemServico.clienteId }?.nome
    val produtoNome = produtoViewModel.listaProdutos.value.find { it.id == ordemServico.produtoId }?.nome
    val funcionarioNome = funcionarioViewModel.listaFuncionarios.value.find { it.id == ordemServico.funcionarioId }?.nome

    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    if (mostrarCaixaDialogo) {
        ExcluirOrdemServico(onConfirm = {
            ordemServico?.let { ordemServicoViewModel.excluirOrdemServico(it) }

            mostrarCaixaDialogo = false
            Toast.makeText(
                context,
                "Ordem de Serviço deletada",
                Toast.LENGTH_LONG
            ).show()

            navController.popBackStack();
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(top = 32.dp) // Distância do topo para evitar sobreposição com a barra de notificações
        ) {
            Text(
                text = if (ordemServico.status == Status.SOLICITADO_BAIXA)
                    "Solicitação de Baixa" else "Informações da Ordem de Serviço",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally) // Centraliza o título
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "ID da ordem de serviço: ${ordemServico.id}", fontWeight = FontWeight.Bold)
                    Text(text = "Cliente: $clienteNome", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Produto: $produtoNome", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Funcionário: $funcionarioNome", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Status: ${ordemServico.status}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Largura: ${ordemServico.largura} m")
                    Text(text = "Altura: ${ordemServico.altura} m")
                    Text(text = "Área: ${ordemServico.largura * ordemServico.altura} m²")
                    Text(text = "Valor por m²: R$ ${ordemServico.valorM2}")
                    Text(text = "Quantidade: ${ordemServico.quantidade}")
                    Text(text = "Valor Unitário: R$ ${ordemServico.valorUnitario}")
                    Text(text = "Valor Total: R$ ${ordemServico.valorTotal}")

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Observações:", fontWeight = FontWeight.Bold)
                    Text(text = "${ordemServico.observacoes}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data e Hora de Abertura: ${
                            ordemServico.dataHorarioAbertura.toLocaleString()
                        }"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp), // Espaço do rodapé
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            when(ordemServico.status){
                Status.SOLICITADO_BAIXA ->{

                }

                Status.BAIXADA -> {

                }
                else ->{

                }
            }


            Button(
                onClick = {
                    mostrarCaixaDialogo = true
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Excluir", color = Color.White)
            }

            if(ordemServico.status != Status.BAIXADA){
                Button(
                    onClick = {
                        val osJson = Gson().toJson(ordemServico);
                        navController.navigate("editarOrdemSerivo/${Gson().toJson(ordemServico)}")
                    },
                    colors = ButtonDefaults.buttonColors(Color.Blue),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("Editar", color = Color.White)
                }

                if(ordemServico.status != Status.SOLICITADO_BAIXA) {
                    Button(onClick = {
                        ordemServicoViewModel.atualizarStatus(ordemServico)
                    }) {
                        Text("Atualizar Status")
                    }
                }else{
                    Button(onClick = {
                        ordemServicoViewModel.atualizarStatus(ordemServico);

                        Toast.makeText(
                            context,
                            "Solicitado Baixa de Ordem de Serviço",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                        Text("Solicitar Baixa")
                    }
                }
            }
        }
    }
}
