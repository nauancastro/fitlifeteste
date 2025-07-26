package com.fernando.fitlife.repository

import com.fernando.fitlife.model.Treino
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun syncFavoritos(favoritos: List<Treino>) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child(userId).child("favoritos").setValue(favoritos)
        }
    }
}