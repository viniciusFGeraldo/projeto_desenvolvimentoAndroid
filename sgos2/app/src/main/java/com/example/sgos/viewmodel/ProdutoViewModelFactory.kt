package com.example.sgos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgos.model.database.dao.ProdutoDao

class ProdutoViewModelFactory(private val produtoDao: ProdutoDao) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(ProdutoViewModel::class.java)){
            return ProdutoViewModel(produtoDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}