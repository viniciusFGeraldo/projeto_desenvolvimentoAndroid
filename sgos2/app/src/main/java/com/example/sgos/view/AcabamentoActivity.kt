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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.sgos.model.entity.Acabamento
import com.example.sgos.viewmodel.AcabamentoViewModel

@Composable
fun ListaAcabamento(acabamentoViewModel: AcabamentoViewModel, navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var acabamentoTemp by remember { mutableStateOf<Acabamento?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    val listaAcabamento by acabamentoViewModel.listaAcabamentos
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirAcabamento(onConfirm = {
            acabamentoTemp?.let { acabamentoViewModel.excluirAcabamento(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "Cadastrar/Atualizar Acabamento",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)  // Cor mais suave para o título
        )
        Spacer(modifier = Modifier.height(25.dp))

        // Campo para Nome do acabamento
        TextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),  // Adiciona padding lateral
            label = { Text(text = "Nome do acabamento") },
            shape = MaterialTheme.shapes.medium,  // Bordas arredondadas médias
            singleLine = true,  // Impede múltiplas linhas
            textStyle = TextStyle(
                fontSize = 16.sp,  // Tamanho da fonte
                fontWeight = FontWeight.Normal,  // Peso da fonte
                color = Color.Black  // Cor do texto
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Campo para Descrição do acabamento
        TextField(
            value = descricao,
            onValueChange = { descricao = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text(text = "Descrição do acabamento") },
            shape = MaterialTheme.shapes.medium,  // Bordas arredondadas médias
            singleLine = true,  // Impede múltiplas linhas
            textStyle = TextStyle(
                fontSize = 16.sp,  // Tamanho da fonte
                fontWeight = FontWeight.Normal,  // Peso da fonte
                color = Color.Black  // Cor do texto
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
            // botão
            onClick = {
                val retorno: String? = if (modoEditar) {
                    acabamentoTemp?.let {
                        acabamentoViewModel.atualizarAcabamento(it.id, nome, descricao).also {
                            modoEditar = false
                            textoBotao = "Salvar"
                        }
                    }
                } else {
                    acabamentoViewModel.salvarAcabamento(nome, descricao)
                }

                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                nome = ""
                descricao = ""
                focusManager.clearFocus()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50), // Cor de fundo verde (o parâmetro correto é containerColor)
                contentColor = Color.White // Cor do texto em branco
            ),
            // botão
            shape = MaterialTheme.shapes.small  // Bordas arredondadas pequenas
        ) {
            Text(text = textoBotao, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Lista de Acabamentos",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A))

        // Lista de acabamentos
        LazyColumn {
            items(listaAcabamento) { acabamento ->
                // Card para cada item
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .shadow(4.dp, shape = MaterialTheme.shapes.medium),  // Sombras para dar efeito de card
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)  // Espaçamento dentro do Card
                    ) {
                        // Nome do acabamento
                        Text(
                            text = "Nome:${acabamento.nome}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),  // Cor do nome
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Descrição do acabamento
                        Text(
                            text = "Descrição:${acabamento.descricao}",
                            fontSize = 14.sp,
                            color = Color(0xFF757575),  // Cor mais suave para a descrição
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Botões de Excluir e Atualizar
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    acabamentoTemp = acabamento
                                    mostrarCaixaDialogo = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(text = "Excluir", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    modoEditar = true
                                    acabamentoTemp = acabamento
                                    nome = acabamento.nome
                                    descricao = acabamento.descricao
                                    textoBotao = "Atualizar"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))  // Verde para o botão de Atualizar
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
fun ExcluirAcabamento(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir este acabamento?") },
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