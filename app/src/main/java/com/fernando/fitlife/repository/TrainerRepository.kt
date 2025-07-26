package com.fernando.fitlife.repository

import com.fernando.fitlife.model.Client
import com.fernando.fitlife.model.Personal
import com.fernando.fitlife.model.Treino
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TrainerRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getClients(): List<Client> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("role", "client")
            .get()
            .await()
        return snapshot.documents.map { doc ->
            Client(
                id = doc.id,
                nome = doc.getString("nome") ?: doc.id
            )
        }
    }

    suspend fun getTrainers(): List<Personal> {
        val snapshot = firestore.collection("users")
            .whereEqualTo("role", "trainer")
            .get()
            .await()
        return snapshot.documents.map { doc ->
            Personal(
                id = 0,
                nome = doc.getString("nome") ?: "",
                especialidade = doc.getString("especialidade") ?: "",
                descricao = doc.getString("descricao") ?: "",
                imagemUrl = doc.getString("fotoUrl") ?: ""
            )
        }
    }

    suspend fun getWorkoutsForClient(clientId: String): List<Treino> {
        val snapshot = firestore.collection("users")
            .document(clientId)
            .collection("treinos")
            .get()
            .await()
        return snapshot.toObjects(Treino::class.java)
    }

    suspend fun addWorkout(clientId: String, treino: Treino) {
        firestore.collection("users")
            .document(clientId)
            .collection("treinos")
            .add(treino)
            .await()
    }
}
