package com.example.sgos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgos.model.database.dao.EquipamentoDao

class EquipamentoViewModelFactory(private val equipamentoDao: EquipamentoDao) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(EquipamentoViewModel::class.java)){
            return EquipamentoViewModel(equipamentoDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}