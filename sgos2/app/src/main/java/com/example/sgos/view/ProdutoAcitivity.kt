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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgos.model.entity.Acabamento
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.model.entity.Produto
import com.example.sgos.viewmodel.AcabamentoViewModel
import com.example.sgos.viewmodel.EquipamentoViewModel
import com.example.sgos.viewmodel.ProdutoViewModel


//TELAS DE PRODUTO
@Composable
fun ListaProdutos(produtoViewModel: ProdutoViewModel, acabamentoViewModel: AcabamentoViewModel, equipamentoViewModel: EquipamentoViewModel, navController: NavController) {

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    var produtoExcluir by remember { mutableStateOf<Produto?>(null) }
    var produtoTemp by remember { mutableStateOf<Produto?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaProdutos by produtoViewModel.listaProdutos
    var listaAcabamentos by acabamentoViewModel.listaAcabamentos
    var listaEquipamentos by equipamentoViewModel.listaEquipamentos

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Variáveis para os dropdowns de acabamento e equipamento
    var acabamentoSelecionado by remember { mutableStateOf<Acabamento?>(null) }
    var equipamentoSelecionado by remember { mutableStateOf<Equipamento?>(null) }

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirProduto(onConfirm = {
            produtoExcluir?.let { produtoViewModel.excluirProduto(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    // Verificando se as listas são nulas ou vazias
    if (listaAcabamentos.isEmpty() || listaEquipamentos.isEmpty()) {
        Toast.makeText(context, "Dados não carregados corretamente.", Toast.LENGTH_LONG).show()
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(text = "Lista de Produtos", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = nome, onValueChange = { nome = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Nome do Produto") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = descricao, onValueChange = { descricao = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Descrição do Produto") })
        Spacer(modifier = Modifier.height(15.dp))

        // ComboBox para selecionar o acabamento
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = { expanded = true }) {
                Text(acabamentoSelecionado?.nome ?: "Selecione um Acabamento")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listaAcabamentos.forEach { acabamento ->
                    DropdownMenuItem(
                        text = {
                            Text(text = acabamento.nome)
                        },
                        onClick = {
                            acabamentoSelecionado = acabamento
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // ComboBox para selecionar o equipamento
        var expanded2 by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = { expanded2 = true }) {
                Text(equipamentoSelecionado?.nome ?: "Selecione um equipamento")
            }
            DropdownMenu(
                expanded = expanded2,
                onDismissRequest = { expanded2 = false }
            ) {
                listaEquipamentos.forEach { equipamento ->
                    DropdownMenuItem(
                        text = { Text(text = equipamento.nome) },
                        onClick = {
                            equipamentoSelecionado = equipamento
                            expanded2 = false
                        }
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(15.dp))

        // Botão de Salvar ou Atualizar
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (acabamentoSelecionado == null || equipamentoSelecionado == null) {
                    Toast.makeText(context, "Selecione acabamento e equipamento.", Toast.LENGTH_LONG).show()
                } else {
                    val retorno: String? = if (modoEditar) {
                        produtoTemp?.let {
                            produtoViewModel.atualizarProduto(
                                it.id,
                                nome,
                                descricao,
                                acabamentoSelecionado!!.id,
                                equipamentoSelecionado!!.id
                            ).also {
                                modoEditar = false
                                textoBotao = "Salvar"
                            }
                        }
                    } else {
                        // Salvar novo produto
                        produtoViewModel.salvarProduto(
                            nome,
                            descricao,
                            acabamentoSelecionado!!.id,
                            equipamentoSelecionado!!.id
                        )
                    }

                    Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                    // Limpar os campos de entrada e foco
                    nome = ""
                    descricao = ""
                    acabamentoSelecionado = null
                    equipamentoSelecionado = null
                    focusManager.clearFocus()
                }
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Lista de produtos
        LazyColumn {
            items(listaProdutos) { produto ->
                Text(
                    text = "${produto.nome} - ${produto.descricao}",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        produtoExcluir = produto
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        produtoTemp = produto
                        nome = produto.nome
                        descricao = produto.descricao
                        acabamentoSelecionado = listaAcabamentos.find { it.id == produto.acabamentoId }
                        equipamentoSelecionado = listaEquipamentos.find { it.id == produto.equipamentoId }
                        textoBotao = "Atualizar"
                    }) {
                        Text(text = "Atualizar")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun ExcluirProduto(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir este produto?") },
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