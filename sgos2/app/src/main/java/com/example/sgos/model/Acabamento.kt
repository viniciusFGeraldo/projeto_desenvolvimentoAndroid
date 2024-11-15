package com.example.sgos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "acabamento")
data class Acabamento(
    @PrimaryKey(autoGenerate = true)
    val acabamentoId: Int = 0,
    val criadoEm: Date,
    val nome: String,
    val descricao: String,
    val dataHorarioAbertura: Date
)
