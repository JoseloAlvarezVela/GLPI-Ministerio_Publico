package com.glpi.glpi_ministerio_pblico.ui.adapter

class Data_TaskUsers {
    private var taskUsersName: String? = null
    private var taskUsersPosition: String? = null //cargo

    fun getTaskUserName(): String{
        return taskUsersName.toString()
    }
    fun setTaskUserName(taskUsersName_: String){
        this.taskUsersName = taskUsersName_
    }

    fun getTaskUserPosition(): String{
        return taskUsersName.toString()
    }
    fun setTaskUserPosition(taskUserPosition_: String){
        this.taskUsersPosition = taskUserPosition_
    }
}