package com.example.huertoavance8.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huertoavance8.ui.screens.login.LoginScreen
import com.example.huertoavance8.ui.screens.login.RegisterScreen
import com.example.huertoavance8.ui.screens.home.HomeScreen
import com.example.huertoavance8.ui.screens.home.DetalleProductoScreen
import com.example.huertoavance8.ui.screens.carrito.CarritoScreen
import com.example.huertoavance8.ui.screens.home.ProductosScreen
import com.example.huertoavance8.ui.screens.perfil.PerfilScreen
import com.example.huertoavance8.ui.screens.perfil.QrScannerScreen
import com.example.huertoavance8.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("detalleProducto") { DetalleProductoScreen(navController) }
        composable("carrito") { CarritoScreen(navController) }
        composable("perfil") { PerfilScreen(    navController) }
        composable("productos") { ProductosScreen(navController) }
        composable("scanner") { QrScannerScreen(navController) }


    }
}
