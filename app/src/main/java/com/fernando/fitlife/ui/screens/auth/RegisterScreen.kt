package com.fernando.fitlife.ui.screens.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
    var passwordVisible by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var trainer by remember { mutableStateOf(false) }
    var especialidade by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var fotoUrl by remember { mutableStateOf("") }
    val role by authViewModel.role.collectAsState()
    val message by authViewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            authViewModel.clearMessage()
        }
    }
    LaunchedEffect(role) {
        role?.let {
            navController.navigate("home") { popUpTo("login") { inclusive = true } }
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = null)
                    }
                }
            )
            OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = trainer, onCheckedChange = { trainer = it })
            Text("Registrar como treinador")
        }
        if (trainer) {
            OutlinedTextField(value = especialidade, onValueChange = { especialidade = it }, label = { Text("Especialidade") })
            OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") })
            OutlinedTextField(value = fotoUrl, onValueChange = { fotoUrl = it }, label = { Text("URL da foto (opcional)") })
        }
        Button(onClick = {
            authViewModel.register(
                email,
                password,
                trainer,
                nome,
                especialidade.takeIf { trainer },
                descricao.takeIf { trainer },
                fotoUrl.takeIf { trainer && fotoUrl.isNotBlank() }
            )
        }) {
            Text("Registrar")
        }
    }
}
}
