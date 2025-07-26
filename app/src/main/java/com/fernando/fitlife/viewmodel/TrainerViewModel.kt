package com.fernando.fitlife.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernando.fitlife.model.Treino
import com.fernando.fitlife.repository.TrainerRepository
import kotlinx.coroutines.launch

class TrainerViewModel : ViewModel() {
    private val repo = TrainerRepository()

    var clients by mutableStateOf<List<String>>(emptyList())
        private set

    fun loadClients() {
        viewModelScope.launch {
            clients = repo.getClients()
        }
    }

    fun addWorkout(clientId: String, treino: Treino) {
        viewModelScope.launch {
            repo.addWorkout(clientId, treino)
        }
    }
}
