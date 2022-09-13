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
    var glpiname_: String? = null
    var glpiRequester_Name_: String? = null
    var glpiRequester_Apellido_: String? = null


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

    fun getGlpiSolReq(): String {
        return GlpiSolReq_.toString()
    }
    fun setGlpiSolReq(GlpiID: String) {
        this.GlpiSolReq_ = GlpiID
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

    fun getGlpiName(): String {
        return glpiname_.toString()
    }
    fun setGlpiName(GlpiName: String) {
        this.glpiname_ = GlpiName
    }

    fun getGlpiRequesterName(): String{
        return glpiRequester_Name_.toString()
    }
    fun setGlpiRequestreName(glpiRequester_Name: String){
        this.glpiRequester_Name_ = glpiRequester_Name
    }

    fun getGlpiRequesterApellido(): String{
        return glpiRequester_Apellido_.toString()
    }
    fun setGlpiRequestreApellido(glpiRequester_Apellido: String){
        this.glpiRequester_Apellido_ = glpiRequester_Apellido
    }
}