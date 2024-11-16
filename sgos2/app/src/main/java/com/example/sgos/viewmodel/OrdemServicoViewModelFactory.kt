package com.example.sgos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgos.model.database.dao.OrdemServicoDao

class OrdemServicoViewModelFactory(private val ordemServicoDao: OrdemServicoDao) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(OrdemServicoViewModel::class.java)){
            return OrdemServicoViewModel(ordemServicoDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }


}