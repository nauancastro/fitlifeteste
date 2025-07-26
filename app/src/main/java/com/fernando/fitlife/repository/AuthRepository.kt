package com.fernando.fitlife.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val currentUser get() = auth.currentUser

    suspend fun login(email: String, password: String): String? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            firestore.collection("users").document(auth.currentUser!!.uid)
                .get().await().getString("role")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String, role: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val uid = auth.currentUser!!.uid
            firestore.collection("users").document(uid).set(mapOf("role" to role)).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun logout() {
        auth.signOut()
    }
}
