package com.fernando.fitlife.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernando.fitlife.model.Client
import com.fernando.fitlife.model.Personal
import com.fernando.fitlife.model.Treino
import com.fernando.fitlife.model.TrainerWorkout
import com.fernando.fitlife.repository.TrainerRepository
import kotlinx.coroutines.launch

class TrainerViewModel : ViewModel() {
    private val repo = TrainerRepository()

    var clients by mutableStateOf<List<Client>>(emptyList())
        private set

    var trainers by mutableStateOf<List<Personal>>(emptyList())
        private set

    var clientWorkouts by mutableStateOf<List<Treino>>(emptyList())
        private set

    var trainerWorkouts by mutableStateOf<List<TrainerWorkout>>(emptyList())
        private set

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun clearMessage() {
        _message.value = null
    }

    fun loadClients() {
        viewModelScope.launch {
            clients = repo.getClients()
        }
    }

    fun loadWorkouts(clientId: String) {
        viewModelScope.launch {
            clientWorkouts = repo.getWorkoutsForClient(clientId)
        }
    }

    fun addWorkout(clientId: String, treino: Treino) {
        viewModelScope.launch {
            repo.addWorkout(clientId, treino)
            loadWorkouts(clientId)
            loadTrainerWorkouts(treino.trainerId)
        }
    }

    fun loadTrainers() {
        viewModelScope.launch {
            trainers = repo.getTrainers()
        }
    }

    fun loadTrainerWorkouts(trainerId: String) {
        viewModelScope.launch {
            trainerWorkouts = repo.getWorkoutsForTrainer(trainerId)
        }
    }

    fun deleteWorkout(clientId: String, workoutId: String, trainerId: String) {
        viewModelScope.launch {
            repo.deleteWorkout(clientId, workoutId)
            loadTrainerWorkouts(trainerId)
        }
    }

    fun uploadImage(clientId: String, workoutId: String, uri: Uri, trainerId: String) {
        viewModelScope.launch {
            val url = repo.uploadWorkoutImage(clientId, workoutId, uri)
            if (url.isNotBlank()) {
                _message.value = "Imagem enviada"
                loadTrainerWorkouts(trainerId)
                loadWorkouts(clientId)
            } else {
                _message.value = "Falha ao enviar imagem"
            }
        }
    }
}
