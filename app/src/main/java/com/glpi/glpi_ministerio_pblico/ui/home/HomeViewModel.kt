package com.glpi.glpi_ministerio_pblico.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        //value = "esto es un fragmento de home"
    }
    val text: LiveData<String> = _text
}