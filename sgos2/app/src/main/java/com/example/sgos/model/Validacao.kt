package com.example.sgos.model

abstract class Validacao {

    companion object {
        private var id = 0

        fun getId(): Int{
            return id++
        }

        fun haCamposEmBranco(nome : String,descricao:String) : Boolean{
            return nome.isBlank() || descricao.isBlank()
        }

        fun haCamposEmBranco(nome: String, cpf: String, rg: String, cep: String, endereco: String, bairro: String, cidade: String, telefone: String) : Boolean{
            return nome.isBlank() || cpf.isBlank() || rg.isBlank() || cep.isBlank() || endereco.isBlank() || bairro.isBlank() || cidade.isBlank() || telefone.isBlank()
        }
    }

}