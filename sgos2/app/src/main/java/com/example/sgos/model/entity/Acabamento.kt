package com.example.sgos.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "acabamento")
data class Acabamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val criadoEm: Date = Date(),
    val nome: String,
    val descricao: String
)

