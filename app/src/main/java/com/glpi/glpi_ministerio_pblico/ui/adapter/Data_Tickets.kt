package com.glpi.glpi_ministerio_pblico.ui.adapter

/*1:
creamos una clase para la lista de tickets y declaramos los atributos que vamos a obtener*/
class Data_Tickets {
    var glpiID_: String? = null
    var glpiTipo_ : String? = null
    var GlpiDescripcion_: String? = null
    var glpiEstado_ : String? = null
    var glpi_currenttime_: String? = null
    var GlpiSolReq_: String? = null
    var glpiNameLogin_: String? = null
    var glpiApellidoLogin_: String? = null
    var glpiRequester_Name_: String? = null
    var glpiRequester_Cargo_: String? = null


    /*Estos métodos getter y setter nos ayudarán a mantener los datos mientras
    llenamos la vista Recycler en la clase Adapter.*/
    fun getGlpiID(): String {
        return glpiID_.toString()
    }
    fun setGlpiID(GlpiID: String) {
        this.glpiID_ = GlpiID
    }

    fun getGlpiTipo(): String{
        return glpiTipo_.toString()
    }
    fun setGlpiTipo(GlpiTipo: String){
        this.glpiTipo_ = GlpiTipo
    }

    fun getGlpiDescripcion(): String{
        return  GlpiDescripcion_.toString()
    }
    fun setGlpiDescripcion(GlpiDescripcion: String){
        this.GlpiDescripcion_ = GlpiDescripcion
    }

    fun getGlpiEstado(): String{
        return glpiEstado_.toString()
    }
    fun setGlpiEstado(glpiEstado: String){
        this.glpiEstado_ = glpiEstado
    }

    fun getCurrentTime(): String {
        return glpi_currenttime_.toString()
    }
    fun setCurrentTime(CurrentTime: String) {
        this.glpi_currenttime_ = CurrentTime
    }

    fun getGlpiNameLogin(): String {
        return glpiNameLogin_.toString()
    }
    fun setGlpiNameLogin(GlpiNameLogin: String) {
        this.glpiNameLogin_ = GlpiNameLogin
    }

    fun getGlpiApellidoLogin(): String {
        return glpiApellidoLogin_.toString()
    }
    fun setGlpiApellidoLogin(GlpiApellidoLogin: String) {
        this.glpiApellidoLogin_ = GlpiApellidoLogin
    }

    fun getGlpiRequesterName(): String{
        return glpiRequester_Name_.toString()
    }
    fun setGlpiRequestreName(glpiRequester_Name: String){
        this.glpiRequester_Name_ = glpiRequester_Name
    }

    fun getGlpiRequesterCargo(): String{
        return glpiRequester_Cargo_.toString()
    }
    fun setGlpiRequestreCargo(glpiRequester_Cargo: String){
        this.glpiRequester_Cargo_ = glpiRequester_Cargo
    }
}