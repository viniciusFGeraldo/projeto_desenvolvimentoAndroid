package com.example.sgos.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sgos.model.database.AppDatabase
import com.example.sgos.model.entity.Acabamento
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.model.entity.Funcionario
import com.example.sgos.model.entity.OrdemServico
import com.example.sgos.model.entity.Produto
import com.example.sgos.model.entity.Status
import com.example.sgos.view.theme.SgosTheme
import com.example.sgos.viewmodel.AcabamentoViewModel
import com.example.sgos.viewmodel.AcabamentoViewModelFactory
import com.example.sgos.viewmodel.ClienteViewModel
import com.example.sgos.viewmodel.ClienteViewModelFactory
import com.example.sgos.viewmodel.EquipamentoViewModel
import com.example.sgos.viewmodel.EquipamentoViewModelFactory
import com.example.sgos.viewmodel.FuncionarioViewModel
import com.example.sgos.viewmodel.FuncionarioViewModelFactory
import com.example.sgos.viewmodel.OrdemServicoViewModel
import com.example.sgos.viewmodel.ProdutoViewModel
import com.example.sgos.viewmodel.ProdutoViewModelFactory

class MainActivity : ComponentActivity() {
    private val acabamentoViewModel : AcabamentoViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).getAcabamentoDao()
        AcabamentoViewModelFactory(dao)
    }

    private val clienteViewModel : ClienteViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).getClienteDao()
        ClienteViewModelFactory(dao)
    }

    private val equipamentoViewModel : EquipamentoViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).getEquipamentoDao()
        EquipamentoViewModelFactory(dao)
    }

    private val funcionarioViewModel : FuncionarioViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).getFuncionarioDao()
        FuncionarioViewModelFactory(dao)
    }

    private val produtoViewModel : ProdutoViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).getProdutoDao()
        ProdutoViewModelFactory(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaProdutos(produtoViewModel, acabamentoViewModel, equipamentoViewModel)
        }
    }


}

//TELAS DE ACABAMENTO
@Composable
fun CadastrarAcabamento(acabamentoViewModel: AcabamentoViewModel){
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = "Cadastro de Acabamento", fontSize = 22.sp)

        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do acabamento") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Nome do descrição") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))

        Button(onClick = {
            if (nome.isNotBlank() && descricao.isNotBlank()){
                acabamentoViewModel.salvarAcabamento(nome, descricao)
                Toast.makeText(context,"Acabamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                nome = ""
                descricao = ""

            }else{
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Salvar Acabamento")
        }

    }

}

@Composable
fun ListaAcabamento(acabamentoViewModel: AcabamentoViewModel){
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var acabamentoTemp by remember { mutableStateOf<Acabamento?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaAcabamento by acabamentoViewModel.listaAcabamentos
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

        Text(text = "Lista de acabamentos", modifier = Modifier.fillMaxWidth(), fontSize = 22.sp)
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = nome, onValueChange = { nome = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Nome do acabamento") })
        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = descricao, onValueChange = { descricao = it }, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Descrição do acabamento") })
        Spacer(modifier = Modifier.height(15.dp))

        Button(modifier = Modifier.fillMaxWidth(),
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
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))


        LazyColumn {
            items(listaAcabamento) { acabamento ->
                Text(text = "${acabamento.nome} (${acabamento.descricao})", modifier = Modifier.fillMaxWidth(), fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        acabamentoTemp = acabamento
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        acabamentoTemp = acabamento
                        nome = acabamento.nome
                        descricao = acabamento.descricao
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
fun ListaClientes(clienteViewModel: ClienteViewModel) {

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
fun ListaEquipamentos(equipamentoViewModel: EquipamentoViewModel) {
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
fun ListaFuncionarios(funcionarioViewModel: FuncionarioViewModel) {
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



//TELAS DE PRODUTO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaProdutos(produtoViewModel: ProdutoViewModel, acabamentoViewModel: AcabamentoViewModel, equipamentoViewModel: EquipamentoViewModel) {

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
        return
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
                        onClick = {
                            acabamentoSelecionado = acabamento
                            expanded = false
                        }
                    ) {
                        Text(text = acabamento.nome)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // ComboBox para selecionar o equipamento
        Text("Selecione o Equipamento")
        Box(modifier = Modifier.fillMaxWidth().clickable {
            var dropdownEquipamentoExpanded = true
        }) {
            Text(
                text = equipamentoSelecionado?.nome ?: "Selecione o equipamento",
                modifier = Modifier.padding(16.dp)
            )
        }
        DropdownMenu(
            expanded = dropdownEquipamentoExpanded,
            onDismissRequest = { dropdownEquipamentoExpanded = false }
        ) {
            listaEquipamentos.forEach { equipamento ->
                DropdownMenuItem(
                    onClick = {
                        equipamentoSelecionado = equipamento
                        dropdownEquipamentoExpanded = false
                    },
                ) {
                    Text(text = equipamento.nome)
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


