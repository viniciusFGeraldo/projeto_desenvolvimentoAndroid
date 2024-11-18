package com.example.sgos.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sgos.model.database.AppDatabase
import com.example.sgos.model.entity.Acabamento
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.Equipamento
import com.example.sgos.model.entity.Funcionario
import com.example.sgos.model.entity.OrdemServico
import com.example.sgos.model.entity.Produto
import com.example.sgos.model.entity.Status
import com.example.sgos.viewmodel.AcabamentoViewModel
import com.example.sgos.viewmodel.AcabamentoViewModelFactory
import com.example.sgos.viewmodel.ClienteViewModel
import com.example.sgos.viewmodel.ClienteViewModelFactory
import com.example.sgos.viewmodel.EquipamentoViewModel
import com.example.sgos.viewmodel.EquipamentoViewModelFactory
import com.example.sgos.viewmodel.FuncionarioViewModel
import com.example.sgos.viewmodel.FuncionarioViewModelFactory
import com.example.sgos.viewmodel.OrdemServicoViewModel
import com.example.sgos.viewmodel.OrdemServicoViewModelFactory
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

    private val ordemServicoViewModel : OrdemServicoViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).getOrdemServicoDao()
        OrdemServicoViewModelFactory(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation(acabamentoViewModel, clienteViewModel, equipamentoViewModel, funcionarioViewModel, produtoViewModel, ordemServicoViewModel)
        }
    }
}

@Composable
fun AppNavigation(
    acabamentoViewModel: AcabamentoViewModel,
    clienteViewModel: ClienteViewModel,
    equipamentoViewModel: EquipamentoViewModel,
    funcionarioViewModel: FuncionarioViewModel,
    produtoViewModel: ProdutoViewModel,
    ordemServicoViewModel: OrdemServicoViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "telaInicial") {
        composable("telaInicial") { TelaInicial(navController) }
        composable("menuCadastros") { MenuCadastros(navController) }
        composable("listaAcabamento") { ListaAcabamento(acabamentoViewModel, navController) }
        composable("listaClientes") { ListaClientes(clienteViewModel, navController) }
        composable("listaEquipamentos") { ListaEquipamentos(equipamentoViewModel, navController) }
        composable("listaFuncionarios") { ListaFuncionarios(funcionarioViewModel, navController) }
        composable("listaProdutos") { ListaProdutos(produtoViewModel, acabamentoViewModel, equipamentoViewModel, navController) }
        composable("listaOrdemServico") { ListaOrdemServico(ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController) }
    }
}

@Composable
fun TelaInicial(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("menuCadastros") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Menu de Cadastros")
        }
        Button(
            onClick = { navController.navigate("listaOrdemServico") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Ordens de Serviço")
        }
    }
}

@Composable
fun MenuCadastros(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lista de botões para cada cadastro
        Button(
            onClick = { navController.navigate("listaAcabamento") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Cadastro de Acabamento")
        }
        Button(
            onClick = { navController.navigate("listaClientes") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Cadastro de Clientes")
        }
        Button(
            onClick = { navController.navigate("listaEquipamentos") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Cadastro de Equipamentos")
        }
        Button(
            onClick = { navController.navigate("listaFuncionarios") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Cadastro de Funcionários")
        }
        Button(
            onClick = { navController.navigate("listaProdutos") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Cadastro de Produtos")
        }
    }
}