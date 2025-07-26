package com.fernando.fitlife.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Adicione a anotação @Entity aqui
@Entity(tableName = "treinos")
data class Treino(
    // Adicione a anotação @PrimaryKey aqui
    @PrimaryKey
    val id: Int,

    val nome: String,
    val descricao: String,
    val imagemUrl: Int,
    val duracaoMin: Int,
    val nivel: String
)