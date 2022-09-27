package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TasksTemplate {
    var idTasksTemplate_: String? = null
    var nombreTasksTemplate_: String? = null
    var contentTasksTemplate_: String? = null
    var id_categoryTasksTemplate_: String? = null
    var categoryTasksTemplate_: String? = null
    var timeTasksTemplate_: String? = null

    fun getIdTasksTemplate():String{
        return idTasksTemplate_.toString()
    }
    fun setIdTasksTemplate(idTasksTemplate: String){
        this.idTasksTemplate_ = idTasksTemplate
    }

    fun getNombreTasksTemplate():String{
        return nombreTasksTemplate_.toString()
    }
    fun setNombreTasksTemplate(nombreTasksTemplate: String){
        this.nombreTasksTemplate_ = nombreTasksTemplate
    }

    fun getContentTasksTemplate():String{
        return contentTasksTemplate_.toString()
    }
    fun setContentTasksTemplate(contentTasksTemplate: String){
        this.contentTasksTemplate_ = contentTasksTemplate
    }

    fun getId_categoryTasksTemplate():String{
        return id_categoryTasksTemplate_.toString()
    }
    fun setId_categoryTasksTemplate(id_categoryTasksTemplate: String){
        this.id_categoryTasksTemplate_ = id_categoryTasksTemplate
    }

    fun getCategoryTasksTemplate():String{
        return categoryTasksTemplate_.toString()
    }
    fun setCategoryTasksTemplate(categoryTasksTemplate: String){
        this.categoryTasksTemplate_ = categoryTasksTemplate
    }

    fun getTimeTasksTemplate():String{
        return timeTasksTemplate_.toString()
    }
    fun setTimeTasksTemplate(timeTasksTemplate: String){
        this.timeTasksTemplate_ = timeTasksTemplate
    }
}