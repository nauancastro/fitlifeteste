package com.fernando.fitlife.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fernando.fitlife.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var trainer by remember { mutableStateOf(false) }
    val role by authViewModel.role.collectAsState()

    LaunchedEffect(role) {
        role?.let {
            if (it == "trainer") {
                navController.navigate("trainer") { popUpTo("login") { inclusive = true } }
            } else {
                navController.navigate("home") { popUpTo("login") { inclusive = true } }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Senha") })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = trainer, onCheckedChange = { trainer = it })
            Text("Registrar como treinador")
        }
        Button(onClick = { authViewModel.register(email, password, trainer) }) {
            Text("Registrar")
        }
    }
}
