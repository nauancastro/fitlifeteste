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
    var duracao by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("") }
    var selectedClient by remember { mutableStateOf<com.fernando.fitlife.model.Client?>(null) }

    LaunchedEffect(Unit) {
        trainerViewModel.loadClients()
    }

    LaunchedEffect(selectedClient) {
        selectedClient?.let { trainerViewModel.loadWorkouts(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do treino") })
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") })
        OutlinedTextField(value = duracao, onValueChange = { duracao = it }, label = { Text("Duração em minutos") })
        OutlinedTextField(value = nivel, onValueChange = { nivel = it }, label = { Text("Nível") })

        Text("Selecione o cliente:")
        LazyColumn(modifier = Modifier.height(150.dp)) {
            items(trainerViewModel.clients) { client ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(client.nome)
                    RadioButton(selected = selectedClient == client, onClick = { selectedClient = client })
                }
            }
        }

        Button(
            onClick = {
                val client = selectedClient
                if (client != null) {
                    val treino = Treino(
                        id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
                        nome = nome,
                        descricao = descricao,
                        duracaoMin = duracao.toIntOrNull() ?: 0,
                        nivel = nivel
                    )
                    trainerViewModel.addWorkout(client.id, treino)
                    nome = ""
                    descricao = ""
                    duracao = ""
                    nivel = ""
                    selectedClient = null
                }
            },
            enabled = selectedClient != null && nome.isNotBlank() && duracao.isNotBlank()
        ) {
            Text("Criar treino")
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (selectedClient != null) {
            Text("Treinos do cliente:")
            LazyColumn(
                modifier = Modifier.height(200.dp)
            ) {
                items(trainerViewModel.clientWorkouts) { treino ->
                    Text("- ${treino.nome}")
                }
            }
        }
    }
}
