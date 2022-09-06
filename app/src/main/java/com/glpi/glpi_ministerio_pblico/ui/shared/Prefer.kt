package com.glpi.glpi_ministerio_pblico.ui.shared

import android.content.Context

class Prefer(val context: Context) {

    val SHARED_DB = "Mydb"
    val TOKEN_DB = "noToken"
    val USER = "noUser"

    val storage = context.getSharedPreferences(SHARED_DB,0)

    //metodo que va a guardar el dato
    fun SaveToken(name:String){
        //editamos el sharedPreference y le pasamos parametro
        storage.edit().putString(TOKEN_DB,name).apply()
    }
    fun SaveUser(name: String){
        storage.edit().putString(USER,name).apply()
    }

    //metodo que recupera los datos del sharedPreference
    fun getToken():String{
        return storage.getString(TOKEN_DB,"noToken")!!
    }

    fun getUser():String{
        return storage.getString(USER,"noUser")!!
    }

    //metodo que borra all content del sharedPreference
    fun delToken(){
        storage.edit().clear().apply()
    }
}