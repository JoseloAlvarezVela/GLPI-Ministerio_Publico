package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_Perfiles {
    var GlpiPerfilID_: String? = null
    var GlpiPerfilLogin_: String? = null
    var GlpiPerfilInterfaz_: String? = null

    /*Estos métodos getter y setter nos ayudarán a mantener los datos mientras
    llenamos la vista Recycler en la clase Adapter.*/
    fun getGlpiPerfilID(): String{
        return GlpiPerfilID_.toString()
    }
    fun setGlpiPerfilID(GlpiPerfilID: String){
        this.GlpiPerfilID_ = GlpiPerfilID
    }

    fun getGlpiPerfilLogin(): String{
        return GlpiPerfilLogin_.toString()
    }
    fun setGlpiPerfilLogin(GlpiPerfilLogin: String){
        this.GlpiPerfilLogin_ = GlpiPerfilLogin
    }

    fun getGlpiPerfilInterfaz(): String{
        return GlpiPerfilInterfaz_.toString()
    }
    fun setGlpiPerfilInterfaz(GlpiPerfilInterfaz: String){
        this.GlpiPerfilInterfaz_ = GlpiPerfilInterfaz
    }
}