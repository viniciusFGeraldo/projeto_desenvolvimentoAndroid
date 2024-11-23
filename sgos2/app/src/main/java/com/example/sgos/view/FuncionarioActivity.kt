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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.sgos.model.entity.Funcionario
import com.example.sgos.viewmodel.FuncionarioViewModel

@Composable
fun ListaFuncionarios(funcionarioViewModel: FuncionarioViewModel, navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var funcionarioTemp by remember { mutableStateOf<Funcionario?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    val listaFuncionarios by funcionarioViewModel.listaFuncionarios
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirFuncionario(onConfirm = {
            funcionarioTemp?.let { funcionarioViewModel.excluirFuncionario(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(Modifier.fillMaxSize().padding(25.dp, 50.dp, 25.dp, 30.dp)) {

        Text(
            text = "Cadastrar/Atualizar Funcionário",
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
                .padding(horizontal = 16.dp),
            label = { Text(text = "Nome do Funcionário") },
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
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Botão de Salvar ou Atualizar
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 60.dp)
                .padding(vertical = 5.dp),
            onClick = {
                val retorno: String? = if (modoEditar) {
                    funcionarioTemp?.let {
                        funcionarioViewModel.atualizarFuncionario(it.id, nome, telefone).also {
                            modoEditar = false
                            textoBotao = "Salvar"
                        }
                    }
                } else {
                    // Salvar novo funcionário
                    funcionarioViewModel.salvarFuncionario(nome, telefone)
                }

                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                // Limpar os campos de entrada e foco
                nome = ""
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

        // Lista de funcionários com separação visual
        LazyColumn {
            item {
                Text(text = "Lista de Funcionários",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A))

                Spacer(modifier = Modifier.height(15.dp))

                if(listaFuncionarios.isEmpty()){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

                        ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Text(text = "Não há nenhum funcionário cadastrado", fontSize = 20.sp)
                        }
                    }
                }
            }
            items(listaFuncionarios) { funcionario ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)  // Espaçamento entre os cards
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        // Nome do Funcionário
                        Text(
                            text = "Nome: ${funcionario.nome}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Telefone do Funcionário
                        Text(
                            text = "Telefone: ${funcionario.telefone}",
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Botões de excluir e atualizar
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Botão Excluir
                            Button(
                                onClick = {
                                    funcionarioTemp = funcionario
                                    mostrarCaixaDialogo = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(text = "Excluir", color = Color.White)
                            }

                            // Botão Atualizar
                            Button(
                                onClick = {
                                    modoEditar = true
                                    funcionarioTemp = funcionario
                                    nome = funcionario.nome
                                    telefone = funcionario.telefone
                                    textoBotao = "Atualizar"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                shape = MaterialTheme.shapes.small
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