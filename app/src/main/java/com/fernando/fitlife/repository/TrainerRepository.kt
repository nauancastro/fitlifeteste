package com.fernando.fitlife.repository

import com.fernando.fitlife.model.Client
import com.fernando.fitlife.model.Personal
import com.fernando.fitlife.model.Treino
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class TrainerRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

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
        val doc = firestore.collection("users")
            .document(clientId)
            .collection("treinos")
            .document()
        doc.set(treino.copy(id = doc.id)).await()
    }

    suspend fun getWorkoutsForTrainer(trainerId: String): List<Pair<String, Treino>> {
        val snapshot = firestore.collectionGroup("treinos")
            .whereEqualTo("trainerId", trainerId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            val clientId = doc.reference.parent.parent?.id ?: return@mapNotNull null
            doc.id to doc.toObject(Treino::class.java)!!.copy(clientId = clientId)
        }
    }

    suspend fun deleteWorkout(clientId: String, workoutId: String) {
        firestore.collection("users")
            .document(clientId)
            .collection("treinos")
            .document(workoutId)
            .delete()
            .await()
    }

    suspend fun uploadWorkoutImage(clientId: String, workoutId: String, uri: Uri): String {
        val ref = storage.reference.child("workouts/$workoutId.jpg")
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        firestore.collection("users")
            .document(clientId)
            .collection("treinos")
            .document(workoutId)
            .update("imagemUrl", url)
            .await()
        return url
    }
}
