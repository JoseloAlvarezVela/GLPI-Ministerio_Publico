package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TasksCategory {
    var idTasksCategory: String? = null
    var nameTasksCategory: String? = null
    var contentTasksCategory: String? = null


    fun getIdTasksCategories():String{
        return idTasksCategory.toString()
    }
    fun setIdTasksCategories(idTasksCategory_: String){
        this.idTasksCategory = idTasksCategory_
    }

    fun getNameTasksCategories():String{
        return nameTasksCategory.toString()
    }
    fun setNameTasksCategories(nameTasksCategory_: String){
        this.nameTasksCategory = nameTasksCategory_
    }

    fun getContentTasksCategories():String{
        return contentTasksCategory.toString()
    }
    fun setContentTasksCategories(contentTasksCategory_: String) {
        this.contentTasksCategory = contentTasksCategory_
    }
}