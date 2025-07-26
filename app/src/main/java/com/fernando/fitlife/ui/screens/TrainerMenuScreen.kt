package com.fernando.fitlife.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fernando.fitlife.model.Treino
import com.fernando.fitlife.viewmodel.TrainerViewModel

@Composable
fun TrainerMenuScreen(
    navController: NavController,
    trainerViewModel: TrainerViewModel = viewModel()
) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var selectedClient by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        trainerViewModel.loadClients()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do treino") })
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") })

        Text("Selecione o cliente:")
        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(trainerViewModel.clients) { client ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(client)
                    RadioButton(selected = selectedClient == client, onClick = { selectedClient = client })
                }
            }
        }

        Button(
            onClick = {
                val client = selectedClient ?: return@onClick
                val treino = Treino(0, nome, descricao, 0, 30, "")
                trainerViewModel.addWorkout(client, treino)
                nome = ""; descricao = ""; selectedClient = null
            },
            enabled = selectedClient != null && nome.isNotBlank()
        ) {
            Text("Criar treino")
        }
    }
}
