package com.example.sgos.view

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sgos.model.database.AppDatabase
import com.example.sgos.model.entity.Cliente
import com.example.sgos.model.entity.OrdemServico
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

    private val produtoViewModel : ProdutoViewModel by viewModels{
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
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "telaInicial") {
        composable("telaInicial") { TelaInicial(navController) }
        composable("menuCadastros") { MenuCadastros(navController, acabamentoViewModel, equipamentoViewModel, context) }
        composable("listaAcabamento") { ListaAcabamento(navController, acabamentoViewModel, produtoViewModel, ordemServicoViewModel) }
        composable("listaClientes") { ListaClientes(clienteViewModel, navController, ordemServicoViewModel) }
        composable("cadastrarCliente") {
            FormClientes(false, clienteViewModel, navController, null)
        }
        composable("editarCliente/{clienteJson}") { backStackEntry ->
            val clienteJson = backStackEntry.arguments?.getString("clienteJson")
            val cliente = Gson().fromJson(clienteJson, Cliente::class.java)
            FormClientes(true, clienteViewModel, navController, cliente)
        }
        composable("listaEquipamentos") { ListaEquipamentos(equipamentoViewModel, produtoViewModel, ordemServicoViewModel) }
        composable("listaFuncionarios") { ListaFuncionarios(funcionarioViewModel, ordemServicoViewModel) }
        composable("listaProdutos") { ListaProdutos(produtoViewModel, acabamentoViewModel, equipamentoViewModel, ordemServicoViewModel) }
        composable("listaOrdemServico") { ListaOrdemServico(false, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController, context) }
        composable("listaSolicitacoesBaixa") { ListaOrdemServico(true, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController, context) }
        composable("editarOrdemSerivo/{osJson}"){ backStackEntry ->
            val osJson = backStackEntry.arguments?.getString("osJson")
            val os = Gson().fromJson(osJson, OrdemServico::class.java)
            FormOrdemServico(true, os, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController)
        }
        composable("cadastroOrdemServico") { FormOrdemServico(false, null, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel, navController) }
        composable("mostrarInformacaoOS/{osId}") {backStackEntry ->
            val osId = backStackEntry.arguments?.getString("osId")
            val os = ordemServicoViewModel.listaOrdemServico.value.find { it.id == osId?.toInt() }
            if(os != null){
                MostrarInformacaoOS(os, navController, ordemServicoViewModel, produtoViewModel,  clienteViewModel, funcionarioViewModel, context)
            }
        }
        composable("SolicitarBaixa/{osId}") {backStackEntry ->
            val osId = backStackEntry.arguments?.getString("osId")
            val os = ordemServicoViewModel.listaOrdemServico.value.find { it.id == osId?.toInt() }

            if(os != null){
                SolicitarBaixa(os, navController, context, ordemServicoViewModel, clienteViewModel, produtoViewModel, funcionarioViewModel)
            }
        }
    }
}

@Composable
fun TelaInicial(navController: NavController) {
    // Estilização da Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Espaçamento em torno de todos os elementos
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Menu Principal",
            style = MaterialTheme.typography.h4.copy(
                color = Color(0xFF000000),  // Cor do texto do título
                fontWeight = FontWeight.Bold
            )
        )

        // Botão Menu de Cadastros
        Button(
            onClick = { navController.navigate("menuCadastros") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Menu de Cadastros",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }

        // Botão Ordens de Serviço
        Button(
            onClick = { navController.navigate("listaOrdemServico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Ordens de Serviço",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }
    }
}

@Composable
fun MenuCadastros(
    navController: NavController,
    acabamentoViewModel: AcabamentoViewModel,
    equipamentoViewModel: EquipamentoViewModel,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Adicionando padding geral
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título do Menu de Cadastros
        Text(
            text = "Menu de Cadastros",
            style = MaterialTheme.typography.h4.copy(
                color = Color(0xFF000000),  // Cor do texto do título
                fontWeight = FontWeight.Bold
            )
        )

        // Botão Cadastro de Acabamento
        Button(
            onClick = { navController.navigate("listaAcabamento") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Acabamento",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }

        // Botão Cadastro de Clientes
        Button(
            onClick = { navController.navigate("listaClientes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Clientes",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }

        // Botão Cadastro de Equipamentos
        Button(
            onClick = { navController.navigate("listaEquipamentos") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Equipamentos",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }

        // Botão Cadastro de Funcionários
        Button(
            onClick = { navController.navigate("listaFuncionarios") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Funcionários",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }

        // Botão Cadastro de Produtos
        Button(
            onClick = {
                if (acabamentoViewModel.listaAcabamentos.value.isEmpty() || equipamentoViewModel.listaEquipamentos.value.isEmpty()) {
                    Toast.makeText(context, "Primeiro cadastre pelo menos um equipamento e um acabamento antes de acessar essa página", Toast.LENGTH_LONG).show()
                } else {
                    navController.navigate("listaProdutos")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),  // Espaçamento entre os botões
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Cor de fundo branco
                contentColor = Color(0xFF3D64FB) // Cor do texto (azul)
            ),
            shape = RoundedCornerShape(12.dp), // Bordas arredondadas
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            border = BorderStroke(2.dp, Color(0xFF3D64FB)) // Borda azul
        ) {
            Text(
                text = "Produtos",
                style = MaterialTheme.typography.h6.copy(color = Color(0xFF3D64FB)) // Cor do texto azul
            )
        }
    }
}


@Composable
fun BackButton(onClick:()->Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier.padding(start = 16.dp)  // Adicionando padding se necessário
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Voltar",
            tint = Color.Black // Ajuste a cor do ícone conforme necessário
        )
    }
}