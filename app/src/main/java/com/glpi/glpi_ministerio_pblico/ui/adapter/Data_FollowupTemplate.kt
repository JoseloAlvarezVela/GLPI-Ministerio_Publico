package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_FollowupTemplate {
    private var idFollowupTemplate: String? = null
    private var nameFollowupTemplate: String? = null
    private var contentFollowupTemplate: String? = null

    fun getIdFollowupTemplates(): String{
        return idFollowupTemplate.toString()
    }
    fun setIdFollowupTemplates(idFollowupTemplate_ : String){
        this.idFollowupTemplate = idFollowupTemplate_
    }

    fun getnameFollowupTemplates(): String{
        return nameFollowupTemplate.toString()
    }
    fun setnameFollowupTemplates(nameFollowupTemplate_ : String){
        this.nameFollowupTemplate = nameFollowupTemplate_
    }

    fun getcontentFollowupTemplates(): String{
        return contentFollowupTemplate.toString()
    }
    fun setcontentFollowupTemplates(contentFollowupTemplate_ : String){
        this.contentFollowupTemplate = contentFollowupTemplate_
    }


}