package com.example.sgos.model

import java.util.Date

data class Cliente(
    val clienteId: Int,
    val dataCadastro: Date,
    val nome: String,
    val cpf: String,
    val rg: String,
    val cep: String,
    val endereco: String,
    val bairro: String,
    val cidade: String,
    val telefone: String
)
