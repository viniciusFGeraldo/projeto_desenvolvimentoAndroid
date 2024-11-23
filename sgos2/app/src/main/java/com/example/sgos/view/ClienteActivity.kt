package com.example.sgos.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.Status
import com.example.sgos.viewmodel.ClienteViewModel
import com.google.gson.Gson

@Composable
fun FormClientes(modoEditar: Boolean, clienteViewModel: ClienteViewModel, navController: NavController, cliente: Cliente?) {
    var nome by remember    { mutableStateOf(if(modoEditar || cliente != null) cliente?.nome    ?: "" else "") }
    var cpf by remember     { mutableStateOf(if(modoEditar || cliente != null) cliente?.cpf     ?: "" else "") }
    var rg by remember      { mutableStateOf(if(modoEditar || cliente != null) cliente?.rg      ?: "" else "") }
    var cep by remember     { mutableStateOf(if(modoEditar || cliente != null) cliente?.nome    ?: "" else "") }
    var endereco by remember{ mutableStateOf(if(modoEditar || cliente != null) cliente?.endereco?: "" else "") }
    var bairro by remember  { mutableStateOf(if(modoEditar || cliente != null) cliente?.bairro  ?: "" else "") }
    var cidade by remember  { mutableStateOf(if(modoEditar || cliente != null) cliente?.cidade  ?: "" else "") }
    var telefone by remember{ mutableStateOf(if(modoEditar || cliente != null) cliente?.telefone?: "" else "") }

    val textoBotao = if(modoEditar) "Editar" else "Salvar"
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(25.dp, 50.dp, 25.dp, 30.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = if(modoEditar) "Editar Cliente" else "Cadastrar Cliente",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "Nome") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = cpf,
            onValueChange = { cpf = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "CPF") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = rg,
            onValueChange = { rg = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "RG") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = cep,
            onValueChange = { cep = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "CEP") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = endereco,
            onValueChange = { endereco = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "Endereço") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = bairro,
            onValueChange = { bairro = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "Bairro") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = cidade,
            onValueChange = { cidade = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "Cidade") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = telefone,
            onValueChange = { telefone = it },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = "Telefone") },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                val retorno: String? = if (modoEditar) {
                    cliente?.let { clienteViewModel.atualizarCliente(it.id, nome, cpf, rg, cep, endereco, bairro, cidade, telefone) }
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

                navController.popBackStack()
            },

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = textoBotao, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(15.dp))

    }
}


@Composable
fun ListaClientes(clienteViewModel: ClienteViewModel, navController: NavController){
    val listaClientes by clienteViewModel.listaClientes

    var mostrarCaixaDialogo by remember { mutableStateOf(false) }
    var clienteTemp by remember { mutableStateOf<Cliente?>(null) }

    if (mostrarCaixaDialogo) {
        ExcluirCliente(onConfirm = {
            clienteTemp?.let { clienteViewModel.excluirCliente(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, 50.dp, 25.dp, 30.dp)
    ){
        LazyColumn {
            item{
                Text(text = "Lista de Clientes",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A))

                Spacer(modifier = Modifier.height(10.dp))

                if(listaClientes.isEmpty()){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                        ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Text(text = "Não há nenhum cliente cadastrado", fontSize = 20.sp)
                        }
                    }
                }
            }
            items(listaClientes) { cliente ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        Text(
                            text = "Nome: ${cliente.nome}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "CPF: ${cliente.cpf}",
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    clienteTemp = cliente
                                    mostrarCaixaDialogo = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(text = "Excluir", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    val clienteJson = Gson().toJson(cliente)
                                    navController.navigate("editarCliente/${clienteJson}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text(text = "Atualizar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(bottom = 60.dp, end = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ){
        LargeFloatingActionButton(
            onClick = { navController.navigate("cadastrarCliente" )},
            Modifier.size(70.dp)
        ) {
            Icon(Icons.Filled.Add, "Floating action button.")
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