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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.viewmodel.EquipamentoViewModel


// TELAS DE EQUIPAMENTO

@OptIn(ExperimentalMaterial3Api::class)
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

        Text(
            text = "Cadastrar/Atualizar Equipamento",
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
            label = { Text(text = "Nome do Equipamento") },
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

        // Campo para Descrição
        TextField(
            value = descricao,
            onValueChange = { descricao = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "Descrição do Equipamento") },
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

        // Botão de Salvar ou Atualizar
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 60.dp)
                .padding(vertical = 5.dp),
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

        Text(text = "Lista de Equipamentos",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A))

        // Lista de equipamentos com separação visual
        LazyColumn {
            items(listaEquipamentos) { equipamento ->
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
                        // Nome do Equipamento
                        Text(
                            text = "Nome: ${equipamento.nome}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Descrição do Equipamento
                        Text(
                            text = "Descrição: ${equipamento.descricao}",
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
                                    equipamentoTemp = equipamento
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
                                    equipamentoTemp = equipamento
                                    nome = equipamento.nome
                                    descricao = equipamento.descricao
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