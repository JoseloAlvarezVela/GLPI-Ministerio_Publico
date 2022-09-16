package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_Entities {
    var GlpiMyEntities_: String? = null

    fun getGlpiMyEntities():String{
        return GlpiMyEntities_.toString()
    }
    fun setGlpiMyEntities(GlpiMyEntities: String){
        this.GlpiMyEntities_ = GlpiMyEntities
    }
}