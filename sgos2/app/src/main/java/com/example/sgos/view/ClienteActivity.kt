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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.sgos.viewmodel.ClienteViewModel

// TELAS DE CLIENTE
@OptIn(ExperimentalMaterial3Api::class)
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

        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "Cadastrar/Atualizar Cliente",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)  // Cor mais suave para o título
        )
        Spacer(modifier = Modifier.height(25.dp))

        // Campo para Nome
        TextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),  // Adiciona padding lateral
            label = { Text(text = "Nome") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para CPF
        TextField(
            value = cpf,
            onValueChange = { cpf = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "CPF") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para RG
        TextField(
            value = rg,
            onValueChange = { rg = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "RG") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para CEP
        TextField(
            value = cep,
            onValueChange = { cep = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "CEP") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para Endereço
        TextField(
            value = endereco,
            onValueChange = { endereco = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "Endereço") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para Bairro
        TextField(
            value = bairro,
            onValueChange = { bairro = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "Bairro") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para Cidade
        TextField(
            value = cidade,
            onValueChange = { cidade = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "Cidade") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para Telefone
        TextField(
            value = telefone,
            onValueChange = { telefone = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "Telefone") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF1A1A1A),
                unfocusedIndicatorColor = Color(0xFFBDBDBD),
                focusedLabelColor = Color(0xFF1A1A1A),
                unfocusedLabelColor = Color(0xFF757575),
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Botão de salvar/atualizar
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 60.dp)
                .padding(vertical = 5.dp),
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
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50), // Cor de fundo verde
                contentColor = Color.White // Cor do texto branco
            ),
            shape = MaterialTheme.shapes.small  // Bordas arredondadas pequenas
        ) {
            Text(text = textoBotao, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Lista de Cliente",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A))

        // Lista de clientes
        LazyColumn {
            items(listaClientes) { cliente ->
                // Card para cada item
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
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text(text = "Atualizar", color = Color.White)
                            }
                        }
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