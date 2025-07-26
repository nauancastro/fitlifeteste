package com.fernando.fitlife.repository

import com.fernando.fitlife.model.Treino
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TrainerRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getClients(): List<String> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("role", "client")
            .get()
            .await()
        return snapshot.documents.map { it.id }
    }

    suspend fun addWorkout(clientId: String, treino: Treino) {
        firestore.collection("users")
            .document(clientId)
            .collection("treinos")
            .add(treino)
            .await()
    }
}
