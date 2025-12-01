package com.example.huertoavance8.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huertoavance8.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {

    val context = LocalContext.current
    val repository = remember { UsuarioRepository(context) }
    val scope = rememberCoroutineScope()

    var usuarioNombre by remember { mutableStateOf("Sin nombre registrado") }
    var usuarioCorreo by remember { mutableStateOf("Sin correo registrado") }

    // Cargar último usuario registrado desde Room
    LaunchedEffect(Unit) {
        scope.launch {
            val usuarios = repository.getAll()
            if (usuarios.isNotEmpty()) {
                val ultimo = usuarios.last()
                usuarioNombre = ultimo.nombre
                usuarioCorreo = ultimo.correo
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                actions = {
                    TextButton(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Cerrar sesión 🔒", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                text = "Perfil de usuario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Nombre", fontWeight = FontWeight.SemiBold)
                    Text(usuarioNombre)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Correo", fontWeight = FontWeight.SemiBold)
                    Text(usuarioCorreo)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔹 Botón para ir al lector QR
            Button(
                onClick = { navController.navigate("scanner") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📷 Escanear código QR")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Volver al inicio
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }


        }
    }
}
