package com.fernando.fitlife.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Adicione a anotação @Entity aqui
@Entity(tableName = "treinos")
data class Treino(
    @PrimaryKey
    val id: Int = 0,
    val nome: String = "",
    val descricao: String = "",
    val imagemUrl: Int = 0,
    val duracaoMin: Int = 0,
    val nivel: String = ""
)