package com.glpi.glpi_ministerio_pblico.ui.shared

import android.content.Context

class Prefer(val context: Context) {

    val SHARED_DB = "Mydb"
    val TOKEN_DB = "noToken"
    val USER_NAME = "noUser"
    val USER_ID = "noUserID"

    val storage = context.getSharedPreferences(SHARED_DB,0)

    //metodo que guarda toke de usuario
    fun SaveToken(name:String){
        //editamos el sharedPreference y le pasamos parametro
        storage.edit().putString(TOKEN_DB,name).apply()
    }

    //metodo que guarda el id del usuario
    fun SaveUserID(name: String){
        storage.edit().putString(USER_ID,name).apply()
    }

    //metodo que guarda el nombre del usuario
    fun SaveUserName(name: String){
        storage.edit().putString(USER_NAME,name)
    }

    //metodo que recupera nombre del sharedPreference
    fun getUserName():String{
        return storage.getString(USER_NAME,"noToken")!!
    }

    //metodo que recupera toke del sharedPreference
    fun getToken():String{
        return storage.getString(TOKEN_DB,"noToken")!!
    }

    //metodo que recupera id del sharedPreference
    fun getUserID():String{
        return storage.getString(USER_ID,"noUser")!!
    }

    //metodo que borra all content del sharedPreference
    fun deleteToken(){
        storage.edit().clear().apply()
    }
}