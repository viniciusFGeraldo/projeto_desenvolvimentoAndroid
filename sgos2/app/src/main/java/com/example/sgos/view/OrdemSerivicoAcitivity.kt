package com.example.sgos.view

import android.content.Context
import android.text.style.BackgroundColorSpan
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgos.model.entity.OrdemServico
import com.example.sgos.model.entity.Status
import com.example.sgos.viewmodel.ClienteViewModel
import com.example.sgos.viewmodel.FuncionarioViewModel
import com.example.sgos.viewmodel.OrdemServicoViewModel
import com.example.sgos.viewmodel.ProdutoViewModel
import com.google.gson.Gson
import java.text.DateFormat


@Composable
fun ListaOrdemServico(
    modoSolicitacaoBaixa: Boolean,
    ordemServicoViewModel: OrdemServicoViewModel,
    clienteViewModel: ClienteViewModel,
    produtoViewModel: ProdutoViewModel,
    funcionarioViewModel: FuncionarioViewModel,
    navController: NavController,
    context: Context
) {
    val listaOrdemServico by ordemServicoViewModel.listaOrdemServico
    val listaClientes by clienteViewModel.listaClientes
    val listaProdutos by produtoViewModel.listaProdutos
    val listaFuncionarios by funcionarioViewModel.listaFuncionarios

    val ordemServicoExcluir by remember { mutableStateOf<OrdemServico?>(null) }
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
            .padding(25.dp, 50.dp, 25.dp, 30.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BackButton(onClick = { navController.popBackStack() })

                    Text(
                        text = "${if (modoSolicitacaoBaixa) "Lista de Solicitações de Baixa" else "Lista de Ordem de Serviço"} ",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black // Usando o vermelho para destacar o título
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    if (!modoSolicitacaoBaixa) {
                        Spacer(modifier = Modifier.weight(0.3f))

                        Button(
                            onClick = { navController.navigate("listaSolicitacoesBaixa") },
                            modifier = Modifier.weight(2f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF084F8C)),
                            shape = RoundedCornerShape(4.dp)

                        ) {
                            Text("Solicitações Baixas", color = Color.White) // Texto branco sobre fundo azul
                        }

                        Spacer(modifier = Modifier.weight(0.3f))

                        Button(
                            onClick = {
                                if (listaClientes.isEmpty() || listaFuncionarios.isEmpty() || listaProdutos.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "Primeiro faça cadastro de pelo menos um: cliente, funcionário e produto para acessar essa página",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    navController.navigate("cadastroOrdemServico")
                                }
                            },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color
                                (0xFF198754)),
                            shape = RoundedCornerShape(4.dp)

                        ) {
                            Text("Cadastrar", color = Color.White) // Texto preto sobre fundo amarelo

                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if ((!modoSolicitacaoBaixa && listaOrdemServico.none { it.status != Status.SOLICITADO_BAIXA }) || (modoSolicitacaoBaixa && listaOrdemServico.none { it.status == Status.SOLICITADO_BAIXA })) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Não há nenhuma ordem de serviço ${if (modoSolicitacaoBaixa) "que foi solicitado baixa" else "cadastrada"}", fontSize = 20.sp, color = Color.Black)
                        }
                    }
                }
            }

            items(listaOrdemServico) { ordemServico ->
                if ((!modoSolicitacaoBaixa && ordemServico.status != Status.SOLICITADO_BAIXA) || (modoSolicitacaoBaixa && ordemServico.status == Status.SOLICITADO_BAIXA)) {
                    val cliente = listaClientes.find { it.id == ordemServico.clienteId }
                    val funcionario = listaFuncionarios.find { it.id == ordemServico.funcionarioId }
                    val produto = listaProdutos.find { it.id == ordemServico.produtoId }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black // Azul para o título da ordem de serviço
                            )
                            cliente?.let {
                                Text(text = "Cliente: ${it.nome}", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                            }
                            funcionario?.let {
                                Text(text = "Funcionário: ${it.nome}", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                            }
                            produto?.let {
                                Text(text = "Produto: ${it.nome}", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                            }
                            Text(
                                text = "Status: ${ordemServico.status}",
                                style = MaterialTheme.typography.bodyMedium,
                                // Verde para indicar um status positivo (aqui pode ser personalizado)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FormOrdemServico(
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

    val listaClientes by clienteViewModel.listaClientes
    val listaProdutos by produtoViewModel.listaProdutos
    val listaFuncionarios by funcionarioViewModel.listaFuncionarios

    var clienteSelecionado by remember { mutableStateOf(if(modoEditar) listaClientes.find { it.id == ordemServico?.clienteId } else null ) }
    var produtoSelecionado by remember { mutableStateOf(if(modoEditar) listaProdutos.find { it.id == ordemServico?.produtoId } else null ) }
    var funcionarioSelecionado by remember { mutableStateOf(if(modoEditar) listaFuncionarios.find { it.id == ordemServico?.funcionarioId } else null ) }

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
            .padding(25.dp, 50.dp, 25.dp, 30.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = if (modoEditar) "Editar Ordem de Serviço" else "Cadastrar Ordem de Serviço",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        var expandedCliente by remember { mutableStateOf(false) }

        OutlinedButton(onClick = { expandedCliente = true }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp) // Bordas arredondadas com raio de 16 dp
        ) {
            Text(clienteSelecionado?.nome ?: "Selecione um Cliente",color = Color(0xFF000000))
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
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
        Spacer(modifier = Modifier.height(6.dp))

        // Funcionário
        var expandedFuncionario by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expandedFuncionario = true }, modifier = Modifier
                .fillMaxWidth(),shape = RoundedCornerShape(4.dp)) {
                Text(funcionarioSelecionado?.nome ?: "Selecione um Funcionario",color = Color
                    (0xFF000000))
            }
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
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
        Spacer(modifier = Modifier.height(6.dp))

        // Produto
        var expadedProduto by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expadedProduto = true }, modifier = Modifier.fillMaxWidth
                (), shape = RoundedCornerShape(4.dp)) {
                Text(produtoSelecionado?.nome ?: "Selecione um Produto",color = Color(0xFF000000))
            }
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expadedProduto,
                onDismissRequest = { expadedProduto = false }
            ) {
                listaProdutos.forEach { produto ->
                    DropdownMenuItem(
                        text = { Text(text = produto.nome) },
                        onClick = {
                            produtoSelecionado = produto
                            expadedProduto = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

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
                           ordemServicoViewModel.atualizarOrdemServico(it.id, ordemServico)
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

                    Toast.makeText(
                        context,
                        retorno,
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(text = if (modoEditar) "Salvar Alterações" else "Cadastrar Ordem de Serviço")
        }

        Spacer(modifier = Modifier.height(30.dp))
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
            ordemServico.let { ordemServicoViewModel.excluirOrdemServico(it) }

            mostrarCaixaDialogo = false
            Toast.makeText(
                context,
                "Ordem de Serviço deletada",
                Toast.LENGTH_LONG
            ).show()

            navController.popBackStack()
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

            CardOrdemServico(ordemServico, clienteNome ?: "", produtoNome ?: "", funcionarioNome ?: "")

            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp), // Espaço do rodapé
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if(ordemServico.status != Status.BAIXADA && ordemServico.status != Status.SOLICITADO_BAIXA){
                Button(
                    onClick = {
                        mostrarCaixaDialogo = true
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Excluir", color = Color.White)
                }

                Spacer(modifier = Modifier.weight(0.25f))

                Button(
                    onClick = {
                        navController.navigate("editarOrdemSerivo/${Gson().toJson(ordemServico)}")
                    },
                    colors = ButtonDefaults.buttonColors(Color.Blue),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar", color = Color.White)
                }

                Spacer(modifier = Modifier.weight(0.25f))
            }

            when (ordemServico.status) {
                Status.PRONTO_PARA_ENTREGA -> {
                    Button(onClick = {
                        navController.navigate("SolicitarBaixa/${ordemServico.id}")
                    },
                        modifier = Modifier.weight(1.5f)
                    ) {
                        Text("Solicitar Baixa")

                    }
                }
                Status.SOLICITADO_BAIXA -> {
                    Button(modifier = Modifier.weight(1.5f),
                        onClick = {
                            ordemServicoViewModel.atualizarStatus(ordemServico)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                        ) {
                        Text("Confirmar Baixa")
                    }
                }
                Status.BAIXADA -> {}

                else -> {
                    Button(modifier = Modifier.weight(1.5f),
                        onClick = {
                            ordemServicoViewModel.atualizarStatus(ordemServico)
                        }) {
                        Text("Atualizar Status")
                    }
                }
            }
        }
    }
}

@Composable
fun CardOrdemServico(ordemServico: OrdemServico, clienteNome:String, produtoNome: String, funcionarioNome: String){
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
            Text(text = "Desconto: R$ ${ordemServico.valorDesconto}")
            Text(text = "Valor A pagar: R$ ${ordemServico.valorAPagar}")

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Observações:", fontWeight = FontWeight.Bold)
            Text(text = ordemServico.observacoes, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Data e Hora de Abertura: ${
                    DateFormat.getDateInstance().format(ordemServico.dataHorarioAbertura)
                }"
            )
        }
    }
}

@Composable
fun SolicitarBaixa(
    ordemServico: OrdemServico,
    navController: NavController,
    context: Context,
    ordemServicoViewModel: OrdemServicoViewModel,
    clienteViewModel: ClienteViewModel,
    produtoViewModel: ProdutoViewModel,
    funcionarioViewModel: FuncionarioViewModel
){
    val clienteNome = clienteViewModel.listaClientes.value.find { it.id == ordemServico.clienteId }?.nome
    val produtoNome = produtoViewModel.listaProdutos.value.find { it.id == ordemServico.produtoId }?.nome
    val funcionarioNome = funcionarioViewModel.listaFuncionarios.value.find { it.id == ordemServico.funcionarioId }?.nome

    var desconto by remember { mutableStateOf("") }
    var valorAPagar by remember { mutableStateOf("") }

    LaunchedEffect(desconto) {
        valorAPagar = "${if(desconto.isNotBlank()) (ordemServico.valorTotal - desconto.toFloat()) else ordemServico.valorTotal}"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp, 50.dp, 25.dp, 30.dp)
    ) {

        Text(
            text = "Solicitar Baixa de Ordem de Serviço",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally) // Centraliza o título
        )

        Spacer(modifier = Modifier.height(16.dp))

        CardOrdemServico(ordemServico, clienteNome ?: "", produtoNome ?: "", funcionarioNome ?: "")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextFieldWithSpacing(
            value = desconto,
            onValueChange = { desconto = it },
            label = "Desconto",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReadOnlyTextFieldWithSpacing(value = valorAPagar, label = "Valor A Pagar")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val descontoFinal = if(desconto.isBlank()) 0f else desconto.toFloat()

            if(descontoFinal > 0 && descontoFinal < valorAPagar.toFloat()){
                val retorno = ordemServicoViewModel.solicitarBaixa(ordemServico.id, descontoFinal)
                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                navController.popBackStack()
                navController.popBackStack()
            }else{
                Toast.makeText(context, "Valor de desconto incorreto!", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Solicitar a Baixa")
        }
    }
}
