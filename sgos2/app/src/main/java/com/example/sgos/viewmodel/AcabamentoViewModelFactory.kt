package com.example.sgos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgos.model.database.dao.AcabamentoDao

class AcabamentoViewModelFactory(private val acabamentoDao: AcabamentoDao) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(AcabamentoViewModel::class.java)){
            return AcabamentoViewModel(acabamentoDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }


}