package com.glpi.glpi_ministerio_pblico.ui.shared

import android.content.Context
import org.json.JSONObject

class Prefer(val context: Context) {

    val SHARED_DB = "Mydb"
    val SHARED_VALIDATE = "myTokenValidate"
    val TOKEN_DB = "noToken"
    val USER_PERFIL = "noPERFIL"
    val USER_ID = "noUserID"

    //json data
    val JSON_ENTITES = "MyJson"
    val storareJson = context.getSharedPreferences(JSON_ENTITES,0)

    val storage = context.getSharedPreferences(SHARED_DB,0)
    val storageValidate = context.getSharedPreferences(SHARED_VALIDATE,0)



    //metodo que guarda el perfil del usuario
    fun setUserPerfil(name: String){
        storageValidate.edit().putString(USER_PERFIL, name.toString()).apply()
    }
    //metodo que recupera nombre del sharedPreference
    fun getUserPerfil(): String? {
        return storageValidate.getString(USER_PERFIL,"null")
    }
    fun deleteUserPerfil(){
        storageValidate.edit().clear().apply()
    }

    //metodo que guarda el nombre del usuario
    /*fun setUserName(name: String){
        storage.edit().putString(USER_NAME,name)
    }*/


    //metodo que guarda toke de usuario
    fun SaveToken(name:String){
        //editamos el sharedPreference y le pasamos parametro
        storage.edit().putString(TOKEN_DB,name).apply()
    }
    //metodo que recupera toke del sharedPreference
    fun getToken():String{
        return storage.getString(TOKEN_DB,"noToken")!!
    }
    //metodo que borra all content del sharedPreference
    fun deleteToken(){
        storage.edit().clear().apply()
    }

    //metodo que recupera id del sharedPreference
    fun getUserID():String{
        return storage.getString(USER_ID,"noUser")!!
    }

    //json data
    fun setJsonEntities(json_content: String){
        storareJson.edit().putString(JSON_ENTITES,json_content).apply()
    }
    fun getJsonEntities(): String{
        return storage.getString(JSON_ENTITES,"noContent")!!
    }

}