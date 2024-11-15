package com.example.sgos.model

import java.util.Date

data class Equipamento(
    val equipamentoId: Int,
    val criadoEm: Date,
    val nome: String,
    val descricao: String,
)
