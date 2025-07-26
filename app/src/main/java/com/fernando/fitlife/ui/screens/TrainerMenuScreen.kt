package com.fernando.fitlife.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
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
    var imagem by remember { mutableStateOf("") }
    var duracao by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("") }
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
        OutlinedTextField(value = imagem, onValueChange = { imagem = it }, label = { Text("Imagem (nome do recurso)") })
        OutlinedTextField(value = duracao, onValueChange = { duracao = it }, label = { Text("Duração em minutos") })
        OutlinedTextField(value = nivel, onValueChange = { nivel = it }, label = { Text("Nível") })

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

        val context = LocalContext.current
        Button(
            onClick = {
                val client = selectedClient
                if (client != null) {
                    val imageId = context.resources.getIdentifier(imagem, "drawable", context.packageName)
                    val treino = Treino(
                        id = 0,
                        nome = nome,
                        descricao = descricao,
                        imagemUrl = imageId,
                        duracaoMin = duracao.toIntOrNull() ?: 0,
                        nivel = nivel
                    )
                    trainerViewModel.addWorkout(client, treino)
                    nome = ""
                    descricao = ""
                    imagem = ""
                    duracao = ""
                    nivel = ""
                    selectedClient = null
                }
            },
            enabled = selectedClient != null && nome.isNotBlank() && duracao.isNotBlank()
        ) {
            Text("Criar treino")
        }
    }
}
