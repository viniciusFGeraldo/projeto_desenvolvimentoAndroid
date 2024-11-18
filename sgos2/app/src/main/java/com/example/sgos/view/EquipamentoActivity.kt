package com.example.sgos.view

import android.widget.Toast
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
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.viewmodel.EquipamentoViewModel


// TELAS DE EQUIPAMENTO
@Composable
fun CadastrarEquipamento(equipamentoViewModel: EquipamentoViewModel) {

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Cadastro de Equipamento", fontSize = 22.sp, modifier = Modifier.padding(bottom = 16.dp))

        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Equipamento") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))


        TextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            if (nome.isNotBlank() && descricao.isNotBlank()) {

                equipamentoViewModel.salvarEquipamento(nome, descricao)
                Toast.makeText(context, "Equipamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                nome = ""
                descricao = ""
            } else {
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Salvar Equipamento")
        }
    }
}

@Composable
fun ListaEquipamentos(equipamentoViewModel: EquipamentoViewModel, navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var equipamentoTemp by remember { mutableStateOf<Equipamento?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaEquipamentos by equipamentoViewModel.listaEquipamentos
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirEquipamento(onConfirm = {
            equipamentoTemp?.let { equipamentoViewModel.excluirEquipamento(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        Text(text = "Lista de Equipamentos", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(15.dp))

        // Campos de entrada
        TextField(value = nome, onValueChange = { nome = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Nome do Equipamento") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = descricao, onValueChange = { descricao = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Descrição do Equipamento") })
        Spacer(modifier = Modifier.height(15.dp))

        // Botão de Salvar ou Atualizar
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                val retorno: String? = if (modoEditar) {
                    equipamentoTemp?.let {
                        equipamentoViewModel.atualizarEquipamento(it.id, nome, descricao).also {
                            modoEditar = false
                            textoBotao = "Salvar"
                        }
                    }
                } else {
                    // Salvar novo equipamento
                    equipamentoViewModel.salvarEquipamento(nome, descricao)
                }

                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                // Limpar os campos de entrada e foco
                nome = ""
                descricao = ""
                focusManager.clearFocus()
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Lista de equipamentos
        LazyColumn {
            items(listaEquipamentos) { equipamento ->
                Text(text = "${equipamento.nome} - ${equipamento.descricao}", modifier = Modifier.fillMaxWidth(), fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        equipamentoTemp = equipamento
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        equipamentoTemp = equipamento
                        nome = equipamento.nome
                        descricao = equipamento.descricao
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
fun ExcluirEquipamento(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir este equipamento?") },
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