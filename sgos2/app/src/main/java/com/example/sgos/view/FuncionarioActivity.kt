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
import com.example.sgos.model.entity.Funcionario
import com.example.sgos.viewmodel.FuncionarioViewModel


//TELAS DE FUNCIONARIO
@Composable
fun CadastrarFuncionario(funcionarioViewModel: FuncionarioViewModel) {

    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Cadastro de Funcionário", fontSize = 22.sp, modifier = Modifier.padding(bottom = 16.dp))

        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Funcionário") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            if (nome.isNotBlank() && telefone.isNotBlank()) {
                funcionarioViewModel.salvarFuncionario(nome, telefone)
                Toast.makeText(context, "Funcionário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                nome = ""
                telefone = ""
            } else {
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Salvar Funcionário")
        }
    }
}

@Composable
fun ListaFuncionarios(funcionarioViewModel: FuncionarioViewModel, navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var funcionarioTemp by remember { mutableStateOf<Funcionario?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaFuncionarios by funcionarioViewModel.listaFuncionarios
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current


    var mostrarCaixaDialogo by remember { mutableStateOf(false) }


    if (mostrarCaixaDialogo) {
        ExcluirFuncionario(onConfirm = {
            funcionarioTemp?.let { funcionarioViewModel.excluirFuncionario(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        Text(text = "Lista de Funcionários", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(15.dp))


        TextField(value = nome, onValueChange = { nome = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Nome do Funcionário") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = telefone, onValueChange = { telefone = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Telefone") })
        Spacer(modifier = Modifier.height(15.dp))


        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                val retorno: String? = if (modoEditar) {
                    funcionarioTemp?.let {
                        funcionarioViewModel.atualizarFuncionario(it.id, nome, telefone).also {
                            modoEditar = false
                            textoBotao = "Salvar"
                        }
                    }
                } else {

                    funcionarioViewModel.salvarFuncionario(nome, telefone)
                }

                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()


                nome = ""
                telefone = ""
                focusManager.clearFocus()
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))


        LazyColumn {
            items(listaFuncionarios) { funcionario ->
                Text(text = "${funcionario.nome} - ${funcionario.telefone}", modifier = Modifier.fillMaxWidth(), fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        funcionarioTemp = funcionario
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        funcionarioTemp = funcionario
                        nome = funcionario.nome
                        telefone = funcionario.telefone
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
fun ExcluirFuncionario(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir este funcionário?") },
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