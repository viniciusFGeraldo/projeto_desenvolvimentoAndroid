package com.example.sgos.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "funcionario")
data class Funcionario(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nome: String,
    val telefone: String,
    val criadoEm: Date = Date(),
)
