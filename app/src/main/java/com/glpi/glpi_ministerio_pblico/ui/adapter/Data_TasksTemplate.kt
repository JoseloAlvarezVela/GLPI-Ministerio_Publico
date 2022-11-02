package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TasksTemplate {
    private var idTasksTemplate: String? = null
    var nameTasksTemplate: String? = null
    var contentTasksTemplate: String? = null
    var idCategoryTasksTemplate: String? = null
    var categoryTasksTemplate: String? = null
    var timeTasksTemplate: String? = null
    var statusTasksTemplate: String? = null

    fun getIdTasksTemplates():String{
        return idTasksTemplate.toString()
    }
    fun setIdTasksTemplates(idTasksTemplate: String){
        this.idTasksTemplate = idTasksTemplate
    }

    fun getNameTasksTemplates():String{
        return nameTasksTemplate.toString()
    }
    fun setNameTasksTemplates(nameTasksTemplate_: String){
        this.nameTasksTemplate = nameTasksTemplate_
    }

    fun getContentTasksTemplates():String{
        return contentTasksTemplate.toString()
    }
    fun setContentTasksTemplates(contentTasksTemplate_: String){
        this.contentTasksTemplate = contentTasksTemplate_
    }

    fun getIdCategoryTasksTemplates():String{
        return idCategoryTasksTemplate.toString()
    }
    fun setIdCategoryTasksTemplates(idCategoryTasksTemplate_: String){
        this.idCategoryTasksTemplate = idCategoryTasksTemplate_
    }

    fun getCategoryTasksTemplates():String{
        return categoryTasksTemplate.toString()
    }
    fun setCategoryTasksTemplates(categoryTasksTemplate_: String){
        this.categoryTasksTemplate = categoryTasksTemplate_
    }


    fun getTimeTasksTemplates():String{
        return timeTasksTemplate.toString()
    }
    fun setTimeTasksTemplates(timeTasksTemplate_: String){
        this.timeTasksTemplate = timeTasksTemplate_
    }

    fun getStatusTasksTemplates():String{
        return statusTasksTemplate.toString()
    }
    fun setStatusTasksTemplates(statusTasksTemplate_: String){
        this.statusTasksTemplate = statusTasksTemplate_
    }
}