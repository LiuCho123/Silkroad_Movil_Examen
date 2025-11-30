package com.example.hollow_knight_silkroad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.hollow_knight_silkroad.Model.AppDatabase
import com.example.hollow_knight_silkroad.Repository.* 
import com.example.hollow_knight_silkroad.View.Components.BottomNavigationBar
import com.example.hollow_knight_silkroad.View.Components.NavigationItem
import com.example.hollow_knight_silkroad.View.Components.bottomNavItems
import com.example.hollow_knight_silkroad.View.Screens.*
import com.example.hollow_knight_silkroad.ViewModel.*
import com.example.hollow_knight_silkroad.ui.theme.Hollow_Knight_SilkroadTheme

@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): VM {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(viewModelStoreOwner = parentEntry)
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hollow_Knight_SilkroadTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val database = remember { AppDatabase.getDatabase(context) }
                val usuarioRepository = remember { UsuarioRepository(context) }
                val hiloRepository = remember { HiloRepository() }
                val respuestaRepository = remember { RespuestaRepository() }
                val triviaRepository = remember { TriviaRepository() }
                val networkChecklistRepository = remember { NetworkChecklistRepository() }

                val loginViewModel = remember { LoginViewModel(usuarioRepository) }
                val registroViewModel = remember { RegistroViewModel(usuarioRepository) }
                val recuperarViewModel = remember { RecuperarContrasenaViewModel(usuarioRepository) }
                val crearHiloViewModel = remember { CrearHiloViewModel(hiloRepository) }
                val triviaViewModel = remember { TriviaViewModel(triviaRepository) }
                val checklistViewModel: ChecklistViewModel = viewModel(factory = ChecklistViewModelFactory(networkChecklistRepository, usuarioRepository))
                val rankingViewModel: RankingViewModel = viewModel(
                    factory = RankingViewModel.RankingViewModelFactory(
                        networkChecklistRepository,
                        usuarioRepository
                    )
                )
                val guiaViewModel: GuiaViewModel = viewModel()


                var startDestination by remember { mutableStateOf("home") }
                var isCheckingSession by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    val haySesion = usuarioRepository.verificarSesionGuardad()
                    if (haySesion) {
                        startDestination = NavigationItem.Foro.route
                    } else {
                        startDestination = "home"
                    }
                    isCheckingSession = false
                }

                if (isCheckingSession) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val rutasConNavBar = bottomNavItems.map { it.route }
                    val mostrarNavBar = currentRoute in rutasConNavBar

                    Scaffold(
                        bottomBar = {
                            if (mostrarNavBar) {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("home") { HomeScreen(navController = navController) }
                            composable("login") { LoginScreen(viewModel = loginViewModel, navController = navController) }
                            composable("register") { RegisterScreen(viewModel = registroViewModel, navController = navController) }

                            navigation(startDestination = "olvidePassword", route = "recuperacionGraph") {
                                composable("olvidePassword") { OlvidePasswordScreen(viewModel = recuperarViewModel, navController = navController) }
                                composable("verificarCodigo") { VerificarCodigoScreen(viewModel = recuperarViewModel, navController = navController) }
                                composable("recuperarPassword") { RecuperarPasswordScreen(viewModel = recuperarViewModel, navController = navController) }
                            }

                        composable(NavigationItem.Foro.route) {
                            val foroViewModel = remember { ForoViewModel(hiloRepository, respuestaRepository, usuarioRepository) }
                            ForoScreen(viewModel = foroViewModel, navController = navController)
                        }
                        composable(NavigationItem.Guia.route) { 
                            GuiaScreen(viewModel = guiaViewModel) 
                        }
                        composable(NavigationItem.Checklist.route) {
                            ChecklistScreen(viewModel = checklistViewModel)
                        }
                            composable(NavigationItem.Ranking.route) {
                                RankingScreen(viewModel = rankingViewModel)
                            }
                        composable(bottomNavItems[4].route) {
                            TriviaScreen(viewModel = triviaViewModel, navController = navController)
                        }

                            composable("crearHilo") { CrearHiloScreen(viewModel = crearHiloViewModel, navController = navController) }

                            composable(
                                route = "hiloDetalle/{hiloId}",
                                arguments = listOf(navArgument("hiloId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val hiloId = backStackEntry.arguments?.getInt("hiloId") ?: 0
                                val hiloDetalleViewModel = remember { HiloDetalleViewModel(hiloRepository, respuestaRepository) }
                                HiloDetalleScreen(hiloId = hiloId, viewModel = hiloDetalleViewModel, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}