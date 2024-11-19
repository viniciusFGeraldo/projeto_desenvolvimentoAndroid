package com.example.sgos.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sgos.model.database.AppDatabase
import com.example.sgos.model.entity.OrdemServico
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
import com.google.gson.Gson

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
        composable("listaOrdemServico") { ListaOrdemServico(false, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController) }
        composable("listaSolicitacoesBaixa") { ListaOrdemServico(true, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController) }
        composable("editarOrdemSerivo/{osJson}"){ backStackEntry ->
            val osJson = backStackEntry.arguments?.getString("osJson")
            val os = Gson().fromJson(osJson, OrdemServico::class.java)
            formOrdemServico(true, os, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController)
        }
        composable("cadastroOrdemServico") { formOrdemServico(false, null, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController) }
        composable("solicitacaoDeBaixa/{osJson}") {backStackEntry ->
            val osJson = backStackEntry.arguments?.getString("osJson")
            val os = Gson().fromJson(osJson, OrdemServico::class.java)
            SolicitarBaixa(os, navController, ordemServicoViewModel)
        }

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