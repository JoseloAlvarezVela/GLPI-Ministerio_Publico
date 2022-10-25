package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_TasksTemplate(
    context: Context,
    private val dataModalArrayListTasksTemplate: ArrayList<Data_TasksTemplate>,
    private val itemTasksTemplateClickListener: onTasksTemplateClickListener
    ):RecyclerView.Adapter<RecycleView_Adapter_TasksTemplate.MyViewTasksTemplateHolder>() {

    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    interface onTasksTemplateClickListener{
        fun onTasksTemplateClick(
            nameTasksTemplate: String,
            contentTasksTemplate: String,
            categoryTasksTemplates: String,
            timeTasksTemplates: String,
            idTasksTemplates: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_TasksTemplate.MyViewTasksTemplateHolder {
        val view = inflater.inflate(R.layout.recycleview_tasks_template, parent, false)

        return MyViewTasksTemplateHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_TasksTemplate.MyViewTasksTemplateHolder,
        position: Int) {

        holder.nameTasksTemplate.text =
            dataModalArrayListTasksTemplate[position].getNameTasksTemplates()
    }

    override fun getItemCount(): Int {

        return dataModalArrayListTasksTemplate.size
    }

    inner class MyViewTasksTemplateHolder(itemTasksTemplate: View): RecyclerView.ViewHolder(itemTasksTemplate){
        var nameTasksTemplate: TextView = itemView.findViewById(R.id.nombreTasksTemplate) as TextView


        init {
            itemView.setOnClickListener {
                itemTasksTemplateClickListener.onTasksTemplateClick(
                    dataModalArrayListTasksTemplate[position].getNameTasksTemplates(),
                    dataModalArrayListTasksTemplate[position].getContentTasksTemplates(),
                    dataModalArrayListTasksTemplate[position].getCategoryTasksTemplates(),
                    dataModalArrayListTasksTemplate[position].getTimeTasksTemplates(),
                    dataModalArrayListTasksTemplate[position].getIdTasksTemplates()
                )
            }
        }
    }
}