package com.example.sgos.model

import java.util.Date

data class Produto(
    val Id: Int,
    val nome: String,
    val descricao: String,
    val largura: Int,
    val altura: Int,
    val valorM2: Int,
    val quantidade: Int,
    val valorUnitario: Int,
    val valorSubTotal: Int,
    val criadoEm: Date,

    val equipamento: Equipamento,
    val acabamento: Acabamento,

)
