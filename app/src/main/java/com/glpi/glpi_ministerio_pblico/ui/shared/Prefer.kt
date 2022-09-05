package com.glpi.glpi_ministerio_pblico.ui.shared

import android.content.Context

class Prefer(val context: Context) {

    val SHARED_DB = "Mydb"
    val TOKEN_DB = "noToken"

    val storage = context.getSharedPreferences(SHARED_DB,0)

    //metodo que va a guardar el dato
    fun SaveToken(name:String){
        //editamos el sharedPreference y le pasamos parametro
        storage.edit().putString(TOKEN_DB,name).apply()
    }

    //metodo que recupera los datos del sharedPreference
    fun getToken():String{
        return storage.getString(TOKEN_DB,"noToken")!!
    }

    //metodo que borra all content del sharedPreference
    fun delToken(){
        storage.edit().clear().apply()
    }
}