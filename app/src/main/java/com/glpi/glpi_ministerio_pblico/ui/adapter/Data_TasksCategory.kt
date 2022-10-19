package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TasksCategory {
    var nameTasksCategory: String? = null
    var contentTasksCategory: String? = null


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