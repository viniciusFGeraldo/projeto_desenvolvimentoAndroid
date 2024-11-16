package com.example.sgos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgos.model.database.dao.FuncionarioDao

class FuncionarioViewModelFactory(private val funcionarioDao: FuncionarioDao) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(FuncionarioViewModel::class.java)){
            return FuncionarioViewModel(funcionarioDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }


}