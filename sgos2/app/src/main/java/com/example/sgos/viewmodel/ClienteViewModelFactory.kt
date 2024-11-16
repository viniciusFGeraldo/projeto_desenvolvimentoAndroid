package com.example.sgos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sgos.model.database.dao.ClienteDao

class ClienteViewModelFactory(private val clienteDao: ClienteDao) : ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)){
            return ClienteViewModel(clienteDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }


}