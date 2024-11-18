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
import com.example.sgos.model.entity.Cliente
import com.example.sgos.viewmodel.ClienteViewModel

// TELAS DE CLIENTE
@Composable
fun CadastrarCliente(clienteViewModel: ClienteViewModel) {

    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var rg by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Cadastro de Cliente", fontSize = 22.sp, modifier = Modifier.padding(bottom = 16.dp))

        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = cpf, onValueChange = { cpf = it }, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = rg, onValueChange = { rg = it }, label = { Text("RG") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = cep, onValueChange = { cep = it }, label = { Text("CEP") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = endereco, onValueChange = { endereco = it }, label = { Text("Endereço") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = bairro, onValueChange = { bairro = it }, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = cidade, onValueChange = { cidade = it }, label = { Text("Cidade") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = {
            if (nome.isNotBlank() && cpf.isNotBlank() && rg.isNotBlank() && cep.isNotBlank() && endereco.isNotBlank() &&
                bairro.isNotBlank() && cidade.isNotBlank() && telefone.isNotBlank()) {

                clienteViewModel.salvarCliente(nome, cpf, rg, cep, endereco, bairro, cidade, telefone)
                Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                nome = ""
                cpf = ""
                rg = ""
                cep = ""
                endereco = ""
                bairro = ""
                cidade = ""
                telefone = ""
            } else {
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Salvar Cliente")
        }
    }
}

@Composable
fun ListaClientes(clienteViewModel: ClienteViewModel, navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var rg by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    var clienteTemp by remember { mutableStateOf<Cliente?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaClientes by clienteViewModel.listaClientes
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    if (mostrarCaixaDialogo) {
        ExcluirCliente(onConfirm = {
            clienteTemp?.let { clienteViewModel.excluirCliente(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        Text(text = "Lista de Clientes", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(15.dp))


        TextField(value = nome, onValueChange = { nome = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Nome") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = cpf, onValueChange = { cpf = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "CPF") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = rg, onValueChange = { rg = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "RG") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = cep, onValueChange = { cep = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "CEP") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = endereco, onValueChange = { endereco = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Endereço") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = bairro, onValueChange = { bairro = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Bairro") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = cidade, onValueChange = { cidade = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Cidade") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = telefone, onValueChange = { telefone = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Telefone") })
        Spacer(modifier = Modifier.height(15.dp))

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                val retorno: String? = if (modoEditar) {
                    clienteTemp?.let {
                        clienteViewModel.atualizarCliente(it.id, nome, cpf, rg, cep, endereco, bairro, cidade, telefone).also {
                            modoEditar = false
                            textoBotao = "Salvar"
                        }
                    }
                } else {

                    clienteViewModel.salvarCliente(nome, cpf, rg, cep, endereco, bairro, cidade, telefone)
                }

                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                nome = ""
                cpf = ""
                rg = ""
                cep = ""
                endereco = ""
                bairro = ""
                cidade = ""
                telefone = ""
                focusManager.clearFocus()
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn {
            items(listaClientes) { cliente ->
                Text(text = "${cliente.nome} - ${cliente.cpf}", modifier = Modifier.fillMaxWidth(), fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        clienteTemp = cliente
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        clienteTemp = cliente
                        nome = cliente.nome
                        cpf = cliente.cpf
                        rg = cliente.rg
                        cep = cliente.cep
                        endereco = cliente.endereco
                        bairro = cliente.bairro
                        cidade = cliente.cidade
                        telefone = cliente.telefone
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
fun ExcluirCliente(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir este cliente?") },
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